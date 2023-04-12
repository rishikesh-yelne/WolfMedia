package edu.ncsu.csc540.s23.backend.controller;

import edu.ncsu.csc540.s23.backend.model.PodcastEpisode;
import edu.ncsu.csc540.s23.backend.service.PodcastEpisodeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/podcast-episode")
public class PodcastEpisodeController {
    private PodcastEpisodeService podcastEpisodeService;

    public PodcastEpisodeController(PodcastEpisodeService podcastEpisodeService) { this.podcastEpisodeService = podcastEpisodeService; }

    @GetMapping("/all")
    public List<PodcastEpisode> getAllPodcastEpisodes() { return podcastEpisodeService.getAllPodcastEpisodes(); }

    @GetMapping("/{podcastEpisodeId}/{podcastId}")
    public PodcastEpisode getPodcastEpisode(@PathVariable Long podcastEpisodeId, @PathVariable Long podcastId) { return podcastEpisodeService.getPodcastEpisode(podcastEpisodeId,podcastId); }
    @PostMapping("/add")
    public Long addPodcastEpisode(@RequestBody PodcastEpisode podcastEpisode) { return podcastEpisodeService.createNewPodcastEpisode(podcastEpisode); }

    @GetMapping("/podcast/{podcastId}")
    public List<PodcastEpisode> getPodcastEpisodesByPodcast(@PathVariable Long podcastId) { return podcastEpisodeService.getPodcastEpisodesByPodcast(podcastId); }

    @PutMapping
    public boolean updatePodcastEpisode(@RequestBody PodcastEpisode podcastEpisode) { return podcastEpisodeService.updatePodcastEpisode(podcastEpisode); }

    @PatchMapping("/{podcastEpisodeId}/{podcastId}")
    public boolean updatePodcastEpisodePodcastId(@PathVariable Long podcastEpisodeId, @PathVariable Long podcastId ) { return podcastEpisodeService.updatePodcastEpisodePodcastId(podcastEpisodeId, podcastId); }
}