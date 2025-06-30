import { Play, Users, Vote } from "lucide-react";
import { Card, CardContent } from "./ui/card";
import type { RelatorioVote } from "@/model/Report";
import { Skeleton } from "./ui/skeleton";

type CardsDataProps = {
  relatorioData: RelatorioVote | undefined;
  isLoading: boolean;
};

export function CardsData({ relatorioData, isLoading }: CardsDataProps) {
  return (
    <div className="mt-4 grid grid-cols-1 gap-4 md:grid-cols-3">
      <Card>
        <CardContent className="p-4">
          <div className="flex items-center gap-3">
            <div className="rounded-lg bg-blue-100 p-2">
              <Vote className="h-5 w-5 text-blue-600" />
            </div>
            <div>
              <p className="text-sm text-gray-600">Total de Pautas</p>
              {isLoading ? (
                <Skeleton className="h-8 w-16" />
              ) : (
                <p className="text-2xl font-bold">
                  {relatorioData?.totalAgenda ?? 0}
                </p>
              )}
            </div>
          </div>
        </CardContent>
      </Card>
      <Card>
        <CardContent className="p-4">
          <div className="flex items-center gap-3">
            <div className="rounded-lg bg-green-100 p-2">
              <Play className="h-5 w-5 text-green-600" />
            </div>
            <div>
              <p className="text-sm text-gray-600">Votações Ativas</p>
              {isLoading ? (
                <Skeleton className="h-8 w-16" />
              ) : (
                <p className="text-2xl font-bold">
                  {relatorioData?.activeSessions ?? 0}
                </p>
              )}
            </div>
          </div>
        </CardContent>
      </Card>
      <Card>
        <CardContent className="p-4">
          <div className="flex items-center gap-3">
            <div className="rounded-lg bg-purple-100 p-2">
              <Users className="h-5 w-5 text-purple-600" />
            </div>
            <div>
              <p className="text-sm text-gray-600">Associados</p>
              {isLoading ? (
                <Skeleton className="h-8 w-16" />
              ) : (
                <p className="text-2xl font-bold">
                  {relatorioData?.activeAssociates ?? 0}
                </p>
              )}
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
