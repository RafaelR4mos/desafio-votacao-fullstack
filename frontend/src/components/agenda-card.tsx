"use client";

import type { Agenda } from "@/model/Agenda";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "./ui/card";
import { Badge } from "./ui/badge";
import { Button } from "./ui/button";
import { BarChart3, Clock, Vote } from "lucide-react";

import { useEffect, useState } from "react";
import { toast } from "sonner";

import NextLink from "next/link";
import { OpenSessionDialog } from "./open-session-dialog";
import { formatDate, formatTime } from "@/utils/dateFormaters";
import { useQuery } from "@tanstack/react-query";
import { getVotesResult } from "@/api/votes";
import { getCountAllAssociate } from "@/api/associate";
import { Progress } from "./ui/progress";

type AgendaCardProps = {
  agendaData: Agenda;
  totalAssociates: number;
};

function formatSecondsToMinutes(seconds: number) {
  return Math.floor(seconds / 60);
}

export function AgendaCard({ agendaData, totalAssociates }: AgendaCardProps) {
  const [tempoRestante, setTempoRestante] = useState(0);
  const [isTimeEnded, setTimeEnded] = useState(false);

  const { data: votesResultData } = useQuery({
    queryFn: () => getVotesResult(agendaData.session.idSession),
    queryKey: ["votes-result", agendaData.session.idSession],
  });

  const { data: associateStatsData } = useQuery({
    queryFn: () => getCountAllAssociate(),
    queryKey: ["associate-stats"],
  });

  function participacaoVotacao(votes: number, totalVoters: number) {
    return (votes * 100) / totalVoters;
  }

  useEffect(() => {
    if (agendaData.session.status === "VOTING" && !isTimeEnded) {
      const interval = setInterval(() => {
        const now = new Date();
        const tempoRestanteMs =
          new Date(agendaData.session.finishedAt).getTime() - now.getTime();

        if (tempoRestanteMs > 0) {
          const novoTempo = Math.ceil(tempoRestanteMs / 1000);
          setTempoRestante(novoTempo);
        } else {
          setTimeEnded(true); // marca que acabou
          setTempoRestante(0); // evita números negativos
          clearInterval(interval); // limpa o intervalo

          toast.info(`Sessão Finalizada`, {
            description: `A votação da pauta ${agendaData.title} foi encerrada!`,
          });
        }
      }, 1000);

      return () => clearInterval(interval);
    }
  }, [agendaData, isTimeEnded]);

  return (
    <Card className="overflow-hidden">
      <CardHeader>
        <div className="flex flex-wrap items-start justify-between">
          <div className="flex-1 space-y-2">
            <div className="flex flex-col items-start gap-2 md:flex-row md:items-center md:gap-3">
              <CardTitle className="max-w-[280px] truncate text-xl md:max-w-[100%]">
                {agendaData.title}
              </CardTitle>
              <Badge
                variant={
                  agendaData.session.status === "DRAFT"
                    ? "secondary"
                    : agendaData.session.status === "VOTING"
                      ? "default"
                      : "outline"
                }
              >
                {agendaData.session.status === "DRAFT" && "Rascunho"}
                {agendaData.session.status === "VOTING" && "Votação Aberta"}
                {agendaData.session.status === "CLOSED" && "Encerrada"}
              </Badge>
            </div>
            <CardDescription className="max-w-[270px] truncate text-base md:max-w-[100%]">
              {agendaData.description}
            </CardDescription>
            <div className="flex flex-col items-start gap-1 text-sm text-gray-500 md:flex-row md:items-center md:gap-4">
              {agendaData.session.status === "CLOSED" && (
                <span>
                  Encerrada em: {formatDate(agendaData.session.finishedAt)}
                </span>
              )}{" "}
              {agendaData.session.status !== "CLOSED" && (
                <span>Criada em: {formatDate(agendaData.createdAt)}</span>
              )}
              <span className="hidden md:block">•</span>
              <span>
                Duração:{" "}
                {formatSecondsToMinutes(agendaData.session.durationInSeconds)}{" "}
                min
              </span>
              <span className="hidden md:block">•</span>
              <span>{totalAssociates} associados</span>
            </div>
          </div>
        </div>
      </CardHeader>

      <CardContent className="space-y-4">
        {/* Timer para sessão ativa */}
        {agendaData.session.status === "VOTING" && (
          <div className="rounded-lg border border-blue-200 bg-blue-50 p-4">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <Clock className="h-4 w-4 text-blue-600" />
                <span className="font-medium text-blue-900">
                  Tempo Restante
                </span>
              </div>
              <span className="font-mono text-2xl font-bold text-blue-600">
                {formatTime(tempoRestante)}
              </span>
            </div>
          </div>
        )}

        {/* Resultados Parciais/Finais */}
        {votesResultData?.data && votesResultData?.data.totalVotes > 0 && (
          <div className="rounded-lg bg-gray-50 p-4">
            <div className="mb-3 flex items-center justify-between">
              <h4 className="flex items-center gap-2 font-medium">
                <BarChart3 className="h-4 w-4" />
                Resultados{" "}
                {agendaData.session.status === "VOTING" ? "Parciais" : "Finais"}
              </h4>
              <span className="text-sm text-gray-600">
                {votesResultData.data.totalVotes}/
                {associateStatsData?.data.totalAssociates} votos (
                {associateStatsData?.data.totalAssociates
                  ? participacaoVotacao(
                      votesResultData.data.totalVotes,
                      associateStatsData?.data.totalAssociates,
                    ).toFixed(1)
                  : 0}
                %)
              </span>
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div className="rounded-lg bg-green-100 p-3 text-center">
                <div className="text-xl font-bold text-green-700">
                  {votesResultData.data.totalVotesYes}
                </div>
                <div className="text-sm text-green-600">SIM</div>
              </div>
              <div className="rounded-lg bg-red-100 p-3 text-center">
                <div className="text-xl font-bold text-red-700">
                  {votesResultData.data.totalVotesNo}
                </div>
                <div className="text-sm text-red-600">NÃO</div>
              </div>
            </div>
            {votesResultData.data &&
              associateStatsData?.data.totalAssociates && (
                <Progress
                  value={participacaoVotacao(
                    votesResultData.data.totalVotes,
                    associateStatsData.data.totalAssociates,
                  )}
                  className="mt-3 h-2"
                />
              )}
          </div>
        )}

        {/* Ações */}
        <div className="flex gap-3 pt-2">
          {agendaData.session.status === "DRAFT" && (
            <OpenSessionDialog
              sessionData={agendaData.session}
              agendaTitle={agendaData.title}
            />
          )}

          {agendaData.session.status === "VOTING" && (
            <NextLink href={`/votar/${agendaData.slug}`}>
              <Button variant="tertiary" className="gap- flex items-center">
                <Vote className="h-4 w-4" />
                Participar da Votação
              </Button>
            </NextLink>
          )}
        </div>
      </CardContent>
    </Card>
  );
}
