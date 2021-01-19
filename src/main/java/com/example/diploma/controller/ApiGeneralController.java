package com.example.diploma.controller;

import com.example.diploma.data.request.GlobalSettingsRequest;
import com.example.diploma.data.request.ModerateRequest;
import com.example.diploma.data.response.GlobalSettingResponse;
import com.example.diploma.data.response.StatisticResponse;
import com.example.diploma.data.response.TagResponse;
import com.example.diploma.data.response.base.ResultResponse;
import com.example.diploma.dto.CalendarDto;
import com.example.diploma.enums.StatisticsType;
import com.example.diploma.service.GlobalSettingService;
import com.example.diploma.service.PostService;
import com.example.diploma.service.StorageService;
import com.example.diploma.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ApiGeneralController {
    private final TagService tagService;
    private final PostService postService;
    private final GlobalSettingService globalSettingService;
    private final StorageService storageService;

    @GetMapping("/tag")
    public ResponseEntity<TagResponse> showTags(@RequestParam(required = false) String query) {
        return new ResponseEntity<>(tagService.getAllTags(), HttpStatus.OK);
    }

    @GetMapping("/calendar")
    public ResponseEntity<CalendarDto> showCalendar(@RequestParam(required = false) Integer year) {
        return new ResponseEntity<CalendarDto>(postService.getCalendar(year), HttpStatus.OK);
    }

    @GetMapping("/statistics/{type}")
    public ResponseEntity<StatisticResponse> showStatistics(@PathVariable StatisticsType type) {
        return globalSettingService.getStatistics(type);
    }

    @PutMapping("/settings")
    @PreAuthorize("hasAuthority('user:moderate')")
    public void setGlobalSettings(@RequestBody GlobalSettingsRequest request) {
        globalSettingService.setGlobalSettings(request);
    }

    @GetMapping("/settings")
    public ResponseEntity<GlobalSettingResponse> getGlobalSettings() {
        GlobalSettingResponse response = new GlobalSettingResponse();
        return globalSettingService.getAllSettings2();
    }


    @PostMapping("/image")
    @PreAuthorize("hasAuthority('user:write')")
    public String handleFileUpload(@RequestParam MultipartFile image) {
        return storageService.handleFileUpload(image);
    }

    @GetMapping(value = "/upload/{path1}/{path2}/{path3}/{fileName}")
    public @ResponseBody
    byte[] getImage(@PathVariable String path1,
                    @PathVariable String path2,
                    @PathVariable String path3,
                    @PathVariable String fileName) {
        String route;

        if (path1.equals("default")) {
            route = new File("").getAbsolutePath()
                    .concat("/upload/9AO/GK2/XK7/NN1.jpg");
        } else {
            route = new File("").getAbsolutePath()
                    .concat("/upload/" + path1 + "/" + path2 + "/" + path3 + "/" + fileName);
        }

        return storageService.getImage(Path.of(route));
    }

    @PostMapping("/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<ResultResponse<?>> moderatePost(@RequestBody ModerateRequest request) {
        return ResponseEntity.ok(new ResultResponse<>(postService.moderatePost(request)));
    }

}
