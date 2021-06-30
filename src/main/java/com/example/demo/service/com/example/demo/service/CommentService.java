package com.example.demo.service;

import com.example.demo.dto.CommentsDto;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.exception.SpringRedditException;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.model.Comment;
import com.example.demo.model.NotificationEmail;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final com.example.demo.service.AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final com.example.demo.service.MailContentBuilder mailContentBuilder;
    private final com.example.demo.service.MailService mailService;

    public void save(CommentsDto commentsDto) throws SpringRedditException {
       Post post = postRepository.findById(commentsDto.getPostId()).orElseThrow(() -> new PostNotFoundException(
                commentsDto.getPostId().toString()
        ));
        Comment comment = commentMapper.map(commentsDto,post, authService.getCurrentUser());
        commentRepository.save(comment);
        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post");
        sendCommentNotification(message, post.getUser());

    }
    private void sendCommentNotification(String message, User user) throws SpringRedditException {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " commented on your post mazafaka",
                user.getEmail(),message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
       Post post = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException(
                postId.toString()
        ));
      return commentRepository.findByPost(post).stream().map(commentMapper::mapToDto).collect(Collectors.toList());
    }


    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName).orElseThrow(()->
                new UsernameNotFoundException("User not found"));
        return commentRepository.findAllByUser(user).stream().map(commentMapper::mapToDto).collect(Collectors.toList());
    }
}
