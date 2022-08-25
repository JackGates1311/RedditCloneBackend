package com.example.sr2_2020.svt2021.projekat.service.impl;

import com.example.sr2_2020.svt2021.projekat.model.Comment;
import com.example.sr2_2020.svt2021.projekat.model.Post;
import com.example.sr2_2020.svt2021.projekat.service.SortService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import static java.lang.Math.abs;
import static java.lang.Math.max;

@Service
@AllArgsConstructor
public class SortServiceImpl implements SortService {

    @Override
    public List<Post> hotSortPosts(List<Post> posts) {

        LinkedHashMap<Post, Double> postsWithScore = new LinkedHashMap<>();

        calculateScore(posts, postsWithScore);

        postsWithScore = postsWithScore.entrySet()
                .stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (entry1, entry2) -> entry2, LinkedHashMap::new));

        return mapToList(postsWithScore);
    }

    @Override
    public List<Comment> hotSortComments(List<Comment> comments) {

        return null;
    }

    private void calculateScore(List<Post> posts, LinkedHashMap<Post, Double> postsWithScore) {

        for(Post post : posts) {

            Long reactionCount = Long.valueOf(post.getReactionCount());

            double order = Math.log10(max(abs(reactionCount), 1));

            int sign;

            if(reactionCount > 0)
                sign = 1;
            else if(reactionCount < 0)
                sign = -1;
            else
                sign = 0;

            long seconds = getEpochSeconds(post.getCreationDate()) - 1134028003;

            double result = sign + order * (seconds / 45000.0);

            postsWithScore.put(post, Math.floor(result * 10000000) / 10000000);

        }

    }

    private List<Post> mapToList(LinkedHashMap<Post, Double> postsWithScore) {

        List<Post> postsSorted = new ArrayList<>();

        for(Map.Entry<Post, Double> e : postsWithScore.entrySet()){

            Post key = e.getKey();

            postsSorted.add(key);
        }

        return postsSorted;
    }

    private Long getEpochSeconds(LocalDateTime date) {

        return date.toInstant(ZoneOffset.UTC).getEpochSecond();
    }

}
