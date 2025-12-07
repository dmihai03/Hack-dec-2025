export interface ArenaQuestion {
  id: string;
  correctAnswer: string;
  reward: number;

  lyric?: string;
  emojis?: string[];
  artist?: string;
  imageUrl?: string;
}
