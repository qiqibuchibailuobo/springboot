package cn.yq.springboot1.service;

import cn.yq.springboot1.dto.PaginationDTO;
import cn.yq.springboot1.dto.QuestionDTO;
import cn.yq.springboot1.dto.QuestionQueryDTO;
import cn.yq.springboot1.exception.CustomizeErrorCode;
import cn.yq.springboot1.exception.CustomizeException;
import cn.yq.springboot1.mapper.QuestionExtMapper;
import cn.yq.springboot1.mapper.QuestionMapper;
import cn.yq.springboot1.mapper.UserMapper;
import cn.yq.springboot1.model.Question;
import cn.yq.springboot1.model.QuestionExample;
import cn.yq.springboot1.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired(required = false)
    private QuestionMapper questionMapper;
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private QuestionExtMapper questionExtMapper;
    /**
     * 主页返回所有问题
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO list(String search,Integer page, Integer size) {

        if(StringUtils.isNotBlank(search)){
            String [] tags = StringUtils.split(search," ");
           search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }


        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage, page);
        //分页size*(page-1)
        Integer offset = size * (page - 1);
        //List<Question> questions = questionMapper.list(offset,size);
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//将question的属性copy到questionDTO上
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    /**
     * 个人信息中心返回个人所提问题
     * @param userId
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        //Integer totalCount = questionMapper.countByUserId(userId);
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(example);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage, page);
        //分页size*(page-1)
        Integer offset = size * (page - 1);


        //List<Question> questions = questionMapper.listByUserId(userId,offset,size);
        QuestionExample questionExample= new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//将question的属性copy到questionDTO上
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }


    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        }else {
            //更新
            question.setGmtModified(question.getGmtCreate());
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());

            int updates =  questionMapper.updateByExampleSelective(updateQuestion,example);
            if (updates != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //            questionMapper.update(question);
        }
    }


    public void incView(Long id) {
        Question question  = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if(StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
         String [] tags = StringUtils.split(queryDTO.getTag(),",");
         String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
         Question question = new Question();
         question.setId(queryDTO.getId());
         question.setTag(regexpTag);
         List<Question> questions = questionExtMapper.selectRelated(question);
         List<QuestionDTO>questionDTOS = questions.stream().map(q -> {
             QuestionDTO questionDTO = new QuestionDTO();
             BeanUtils.copyProperties(q,questionDTO);
             return  questionDTO;
         }).collect(Collectors.toList());
        return questionDTOS;
    }
}
