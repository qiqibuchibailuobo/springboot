/**
 * 提交回复
 */
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1, content);
    // console.log(questionId);
    // console.log(content);
}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("回复内容为空")
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                // $("#comment_section").hide();//隐藏回复框
                // alert("回复成功！")
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);//弹出是否登录窗口，如点击确定就打开登录窗口
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=7dcd2592d6a8a8ba3852&redirect_uri=http://localhost:7777/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                        <!--如何在弹出框登录页面并关闭-->
                    }
                } else {
                    alert(response.message)
                }
            }
        },
        dataType: "json"
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);

}

function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
        e.classList.add("icon");

    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {//避免重复加载数据
            //展开二级评论
            comments.addClass("in");
            //标记展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.remove("icon");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                var subCommentContainer = $("#comment-" + id);
                // var commentBody = $("comment-body-" + id);
                $.each(data.data.reverse(), function (index, comment) {
                    //二级评论页面显示左边部分
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));
                    //二级评论页面显示中间部分
                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html":  moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    //二级评论页面显示整体部分
                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                //标记展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.remove("icon");
                e.classList.add("active");
            });
        }

    }
}


/**
 * 发布问题标签选择
 * @param value
 */
function  selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous =  $("#tag").val();
    if(previous.indexOf(value) == -1){
        if(previous){
            $("#tag").val(previous +','+ value);
        }else {
            $("#tag").val(value);
        }
    }

}
function showSelectTag() {
   $("#select-tag").show();




    //     $("#tag").focus(function(){
    //         $("#select-tag").show();
    //     });
    //
    // $("#select-tag").οnclick=function(){
    //
    //     $("#tag").focus();
    // }
        // document.getElementById('tag').οnmοusedοwn=function(e){
        //     //现代浏览器阻止默认事件
        //     if ( e && e.preventDefault )
        //         e.preventDefault();
        //     //IE阻止默认事件
        //     else
        //         window.event.returnValue = false;
        //     return false;
        // };
        // $("#tag").blur(function(){
        //     $("#select-tag").attr("style","display:none");
        // });

}
