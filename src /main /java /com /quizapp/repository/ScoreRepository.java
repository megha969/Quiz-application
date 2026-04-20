package com.quizapp.repository;

import com.quizapp.model.ScoreEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In-memory leaderboard repository.
 */
public class ScoreRepository {

    private final List<ScoreEntry> scores = new ArrayList<>();

    public void save(ScoreEntry entry) {
        scores.add(entry);
        Collections.sort(scores);
    }

    public List<ScoreEntry> getTopScores(int limit) {
        return scores.stream()
                .sorted()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<ScoreEntry> getScoresByPlayer(String playerName) {
        return scores.stream()
                .filter(s -> s.getPlayerName().equalsIgnoreCase(playerName))
                .sorted()
                .collect(Collectors.toList());
    }

    public List<ScoreEntry> getScoresByCategory(String category) {
        return scores.stream()
                .filter(s -> s.getCategory().equalsIgnoreCase(category))
                .sorted()
                .collect(Collectors.toList());
    }

    public List<ScoreEntry> getAllScores() {
        return Collections.unmodifiableList(scores);
    }

    public void clearAll() {
        scores.clear();
    }

    public int getTotalGames() {
        return scores.size();
    }
}
