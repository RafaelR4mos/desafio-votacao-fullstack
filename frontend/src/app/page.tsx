"use client";

import { getReportVote } from "@/api/report";
import { CardsData } from "@/components/report-cards";
import { Header } from "@/components/header";
import { Button } from "@/components/ui/button";
import { useQuery } from "@tanstack/react-query";
import { Plus } from "lucide-react";

import NextLink from "next/link";
import { AgendaList } from "@/components/agenda-list";

export default function Home() {
  const { data: relatorioData, isLoading: isLoadingRelatorio } = useQuery({
    queryFn: getReportVote,
    queryKey: ["relatorio"],
  });

  return (
    <div className="min-h-screen bg-gray-50">
      <Header>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">
            Sistema de Votação Cooperativa
          </h1>
          <p className="mt-1 text-gray-600">
            Gerencie e participe das votações da cooperativa
          </p>
        </div>
        <Button asChild>
          <NextLink href="/criar-pauta" className="flex items-center gap-2">
            <Plus className="h-4 w-4" />
            Nova Pauta
          </NextLink>
        </Button>
      </Header>
      <div className="m-auto h-full max-w-[100%] px-4 md:max-w-[80%]">
        <CardsData
          isLoading={isLoadingRelatorio}
          relatorioData={relatorioData?.data}
        />

        <div className="mt-4">
          <AgendaList
            totalAssociates={relatorioData?.data.activeAssociates || 0}
          />
        </div>
      </div>
    </div>
  );
}
