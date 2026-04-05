import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post } from '../model/post';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private postUrl = 'http://localhost:8080/api/v1/posts';
  private interactionUrl = 'http://localhost:8080/api/v1/interactions';

  constructor(private http:HttpClient){}

  getFeed(): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.postUrl}/feed`);
  }

  createPost(content: string): Observable<Post> {
    return this.http.post<Post>(this.postUrl, { content });
  }

  likePost(postId: number): Observable<void>{
    return this.http.post<void>(`${this.interactionUrl}/${postId}/likes`,{});
  }

  addComment(postId: number, content: string) {
    return this.http.post(
      `${this.interactionUrl}/${postId}/comments`,
      { content }
    );
  }

  getComments(postId: number) {
    return this.http.get<any[]>(`${this.interactionUrl}/${postId}/comments`);
  }
}
