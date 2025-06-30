"use client";

import { useState } from "react";
import { Button } from "./ui/button";
import type { SessionStatus } from "@/model/Session";
import { useQuery } from "@tanstack/react-query";
import { findAll, findAllBySessionStatus } from "@/api/agenda";
import { AgendaCard } from "./agenda-card";
import { Skeleton } from "./ui/skeleton";
import { XCircle } from "lucide-react";

type AgendaListProps = {
  totalAssociates: number;
};

export function AgendaList({ totalAssociates }: AgendaListProps) {
  const [selectedAgendaStatus, setSelectedAgendaStatus] = useState<
    SessionStatus | "ALL"
  >("ALL");

  const agendaText = {
    VOTING: "em votação",
    DRAFT: "em rascunho",
    CLOSED: "Encerradas",
    ALL: "",
  };

  const { data: agendaData, isLoading: isLoadingAgenda } = useQuery({
    queryFn: () => {
      if (selectedAgendaStatus !== "ALL") {
        return findAllBySessionStatus(selectedAgendaStatus);
      }

      return findAll();
    },
    queryKey: ["agendas", selectedAgendaStatus],
  });

  return (
    <div>
      <div className="flex flex-col flex-wrap items-start justify-between md:flex-row md:items-center">
        <h2 className="text-2xl font-semibold">
          Pautas {agendaText[selectedAgendaStatus]}
        </h2>
        <div className="flex flex-wrap items-center gap-2">
          <Button
            variant={selectedAgendaStatus === "ALL" ? "default" : "outline"}
            onClick={() => setSelectedAgendaStatus("ALL")}
          >
            Todas
          </Button>
          <Button
            variant={selectedAgendaStatus === "VOTING" ? "default" : "outline"}
            onClick={() => setSelectedAgendaStatus("VOTING")}
          >
            Em votação
          </Button>
          <Button
            variant={selectedAgendaStatus === "DRAFT" ? "default" : "outline"}
            onClick={() => setSelectedAgendaStatus("DRAFT")}
          >
            Rascunho
          </Button>
          <Button
            variant={selectedAgendaStatus === "CLOSED" ? "default" : "outline"}
            onClick={() => setSelectedAgendaStatus("CLOSED")}
          >
            Encerrada
          </Button>
        </div>
      </div>

      <div className="mt-5 flex w-full flex-col gap-4">
        {!isLoadingAgenda ? (
          agendaData?.data && agendaData.data.length > 0 ? (
            agendaData.data
              .sort((a) =>
                a.session.status == "VOTING"
                  ? -1
                  : a.session.status == "DRAFT"
                    ? -0.5
                    : 1,
              )
              .map((agenda) => (
                <AgendaCard
                  key={agenda.idAgenda}
                  agendaData={agenda}
                  totalAssociates={totalAssociates}
                />
              ))
          ) : (
            <div className="h-20 w-full bg-yellow-50 p-4">
              <span className="flex items-center gap-2">
                <XCircle /> Sem pautas com o status selecionado.
              </span>{" "}
            </div>
          )
        ) : (
          // Put at the top card with "VOTING" and "DRAFT" status.
          <div className="flex flex-col gap-4">
            {Array.from({ length: 4 }).map((_, i) => (
              <Skeleton key={i} className="h-52 w-full" />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
