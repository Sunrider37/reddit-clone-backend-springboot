package com.example.demo.repository;

import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {

    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(@NotNull Post post, User currentUser);
}
