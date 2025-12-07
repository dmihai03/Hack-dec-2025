import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  // Event emitted when user joins a group
  private groupJoinedSource = new Subject<void>();
  groupJoined$ = this.groupJoinedSource.asObservable();

  // Event emitted when user leaves a group
  private groupLeftSource = new Subject<void>();
  groupLeft$ = this.groupLeftSource.asObservable();

  // Event emitted when a star is given to a shared song
  private starGivenSource = new Subject<void>();
  starGiven$ = this.starGivenSource.asObservable();

  emitGroupJoined() {
    this.groupJoinedSource.next();
  }

  emitGroupLeft() {
    this.groupLeftSource.next();
  }

  emitStarGiven() {
    this.starGivenSource.next();
  }
}
