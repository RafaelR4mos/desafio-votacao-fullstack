import { api } from "@/lib/axios";
import type { Agenda, AgendaCreate } from "@/model/Agenda";
import type { SessionStatus } from "@/model/Session";
import type { AxiosResponse } from "axios";

const ENDPOINT_PREFIX = "/agenda";

export async function createAgenda({
  title,
  description,
  durationInSeconds,
}: AgendaCreate): Promise<AxiosResponse<Agenda>> {
  return await api.post(`${ENDPOINT_PREFIX}`, {
    title,
    description,
    durationInSeconds,
  });
}

export async function findAllBySessionStatus(
  status: SessionStatus,
): Promise<AxiosResponse<Agenda[]>> {
  return await api.get(`${ENDPOINT_PREFIX}/by-status`, {
    params: { s: status },
  });
}

export async function findBySlug(slug: string): Promise<AxiosResponse<Agenda>> {
  return await api.get(`${ENDPOINT_PREFIX}/${slug}`);
}

export async function findAll(): Promise<AxiosResponse<Agenda[]>> {
  return await api.get(`${ENDPOINT_PREFIX}`);
}
