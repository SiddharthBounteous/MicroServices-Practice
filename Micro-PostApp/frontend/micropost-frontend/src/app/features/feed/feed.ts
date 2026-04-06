import { Component, ChangeDetectorRef } from '@angular/core';
import { PostService } from '../../core/services/post';
import { Post } from '../../core/model/post';
import { Comment } from '../../core/model/comment';

@Component({
  selector: 'app-feed',
  standalone: false,
  templateUrl: './feed.html',
  styleUrls: ['./feed.scss'],
})
export class FeedComponent {
  posts: Post[] = [];
  newPostContent = '';
  postError = '';
  commentInputs: { [postId: number]: string } = {};
  showComments: { [postId: number]: boolean } = {};
  commentsMap: { [postId: number]: Comment[] } = {};

  constructor(
    private postService: PostService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadFeed();
  }

  loadFeed(): void {
    this.postService.getFeed().subscribe({
      next: (data: Post[]) => {
        this.posts = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to load feed', err)
    });
  }

  submitPost(): void {
    const content = this.newPostContent.trim();
    if (!content) return;

    if (content.length > 140) {
      this.postError = 'Post cannot exceed 140 characters';
      return;
    }

    this.postService.createPost(content).subscribe({
      next: () => {
        this.newPostContent = '';
        this.loadFeed();
      },
      error: (err) => {
        console.error('Failed to create post', err);

        if(err.error){
          this.postError=err.error;
        }
        else{
          this.postError='Something went wrong';
        }
      }
    });
  }

  toggleLike(post: Post): void {
    this.postService.likePost(post.postId).subscribe({
      next: () => {
        this.loadFeed();
      },
      error: (err) => console.error('Failed to like post', err)
    });
  }

  submitComment(post: Post): void {
    const content = this.commentInputs[post.postId]?.trim();
    if (!content) return;

    this.postService.addComment(post.postId, content).subscribe({
      next: () => {
        this.commentInputs[post.postId] = '';

        //reload comments for that post
        this.postService.getComments(post.postId).subscribe((comments) => {
          this.commentsMap[post.postId] = comments;
          this.posts = this.posts.map(p =>
            p.postId === post.postId
              ? { ...p, commentCount: comments.length }
              : p
          );
          this.cdr.detectChanges();
        });
      },
      error: (err) => console.error('Failed to add comment', err)
    });
  }

  toggleComments(post: Post): void {
    const postId = post.postId;

    // toggle visibility
    this.showComments[postId] = !this.showComments[postId];

    // load only first time
    if (this.showComments[postId] && !this.commentsMap[postId]) {
      this.postService.getComments(postId).subscribe({
        next: (comments) => {
          this.commentsMap[postId] = comments;
          this.cdr.detectChanges();
        },
        error: (err) => console.error('Failed to load comments', err)
      });
    }
  }
}