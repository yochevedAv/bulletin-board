package com.example.bulletinboard.ui.createPost;

import com.example.bulletinboard.ApiClient;
import com.example.bulletinboard.BulletinBoardService;

public class CreatePostDataSource {
    ApiClient apiClient = ApiClient.getInstance();
    BulletinBoardService bulletinBoardService = apiClient.getBulletinBoardService();
}
