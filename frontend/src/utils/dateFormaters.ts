import { format } from "date-fns";
import { ptBR } from "date-fns/locale/pt-BR";

export function formatTime(segundos: number) {
  const mins = Math.floor(segundos / 60);
  const secs = segundos % 60;
  return `${mins}:${secs.toString().padStart(2, "0")}`;
}

export function formatDate(data: string) {
  return format(new Date(data), "dd/MM/yyyy 'às' HH:mm", {
    locale: ptBR,
  });
}

export function formatSecondsToMinutes(seconds: number) {
  return Math.floor(seconds / 60);
}
