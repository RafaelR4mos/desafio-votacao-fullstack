export type SessionStatus = "DRAFT" | "VOTING" | "CLOSED";

export type Session = {
  idSession: string;
  status: SessionStatus;
  durationInSeconds: number;
  startedAt: string;
  finishedAt: string;
  createdAt: string;
};
