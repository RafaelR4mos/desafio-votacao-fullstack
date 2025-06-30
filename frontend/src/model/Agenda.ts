import type { Session } from "./Session";

export type Agenda = {
  idAgenda: number;
  title: string;
  slug: string;
  description: string;
  session: Session;
  createdAt: string;
  updatedAt: string;
};

export type AgendaCreate = {
  title: string;
  description: string;
  durationInSeconds: number;
};
