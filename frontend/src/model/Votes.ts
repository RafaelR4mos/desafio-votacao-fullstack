export type TypeVote = "SIM" | "NAO";

export type Vote = {
  idSession: string;
  idAssociate: string;
  vote: TypeVote;
  votedAt: string;
};

export type VoteCreate = {
  idSession: string;
  idAssociate: string;
  vote: TypeVote;
};

export type VoteCheck = {
  idSession: string;
  idAssociate: string;
};

export type VoteCheckResponse = {
  message: string;
  alreadyVoted: boolean;
  votedAt: string;
};

export type VoteResultsResponse = {
  totalVotesYes: number;
  totalVotesNo: number;
  totalVotes: number;
};
