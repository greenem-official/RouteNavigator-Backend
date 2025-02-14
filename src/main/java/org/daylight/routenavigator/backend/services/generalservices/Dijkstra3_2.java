package org.daylight.routenavigator.backend.services.generalservices;

import lombok.ToString;
import org.daylight.routenavigator.backend.entities.Location;
import org.daylight.routenavigator.backend.entities.Route;
import org.daylight.routenavigator.backend.services.entitysaervices.LocationService;
import org.daylight.routenavigator.backend.services.entitysaervices.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;

public class Dijkstra3_2 {
    private static RouteService routeService;

    public static void setRouteService(RouteService routeService) {
        Dijkstra3_2.routeService = routeService;
    }

    static class NodeTime {
        Location node;
        OffsetDateTime time;

        NodeTime(Location node, OffsetDateTime time) {
            this.node = node;
            this.time = time;
        }

        @Override
        public String toString() {
            return "NodeTime [node=" + node + ", time=" + time + "]";
        }
    }

    public static class Path {
        OffsetDateTime time;  // Время прибытия
        List<Route> edges;    // Рёбра, из которых состоит путь

        Path(OffsetDateTime time, List<Route> edges) {
            this.time = time;
            this.edges = new ArrayList<>(edges); // Копируем список для иммутабельности
        }

        // Опционально: метод для получения списка локаций пути
        public List<Location> getNodes() {
            List<Location> nodes = new ArrayList<>();
            if (!edges.isEmpty()) {
                nodes.add(edges.get(0).getDepartureLocation());
                for (Route edge : edges) {
                    nodes.add(edge.getArrivalLocation());
                }
            }
            return nodes;
        }

        @Override
        public String toString() {
            return "Path{" +
                    "time=" + time +
                    ", edges=" + edges +
                    '}';
        }
    }

    // Функция, которая возвращает доступные рёбра из вершины в заданное время
    private static List<Route> getAvailableEdges(Location node, OffsetDateTime currentTime) {
        return routeService.findAllByDepartureLocationAndDepartureTimeAfter(node, currentTime);
    }

    public static List<Path> temporalDijkstraK(Location start, Location end, OffsetDateTime startTime, int K) {
        // Приоритетная очередь для выбора вершины с минимальным временем
        PriorityQueue<NodeTime> queue = new PriorityQueue<>(Comparator.comparingLong(nt -> nt.time.toEpochSecond()));
        queue.add(new NodeTime(start, startTime));

        // Словарь для хранения K лучших путей до каждой вершины
        Map<Location, List<Path>> arrivalPaths = new HashMap<>();
        arrivalPaths.put(start, new ArrayList<>(Collections.singletonList(
                new Path(startTime, new ArrayList<>()) // Путь начинается без рёбер
        )));

        // Список для хранения K лучших путей до конечной вершины
        List<Path> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            NodeTime current = queue.poll();
            Location currentNode = current.node;
            OffsetDateTime currentArrivalTime = current.time;

            // Если достигли конечной вершины, добавляем путь в результат
            if (currentNode.equals(end)) {
                for (Path path : arrivalPaths.get(currentNode)) {
                    result.add(path);
                    if (result.size() >= K) {
                        return result;
                    }
                }
            }

            // Получаем доступные рёбра из текущей вершины, отправляющиеся не раньше текущего времени
            List<Route> edges = getAvailableEdges(currentNode, currentArrivalTime);

            // Перебираем все доступные рёбра
            for (Route edge : edges) {
                OffsetDateTime departureTime = edge.getDepartureTime();
                Duration duration = edge.getDuration();
                Location neighbor = edge.getArrivalLocation();

                // Разрешены рёбра с departureTime >= currentArrivalTime
                if (!departureTime.isBefore(currentArrivalTime)) {
                    OffsetDateTime arrivalTime = departureTime.plus(duration);

                    // Получаем список путей для текущей вершины
                    List<Path> currentPaths = arrivalPaths.get(currentNode);

                    // Для каждого пути до текущей вершины создаём новый путь до соседней вершины
                    for (Path currentPath : currentPaths) {
                        // Создаём новый путь с добавлением текущего ребра
                        List<Route> newEdges = new ArrayList<>(currentPath.edges);
                        newEdges.add(edge); // Добавляем ребро в путь
                        Path newPath = new Path(arrivalTime, newEdges);

                        // Получаем список путей для соседней вершины
                        List<Path> neighborPaths = arrivalPaths.getOrDefault(neighbor, new ArrayList<>());

                        // Проверяем, есть ли уже такой путь в neighborPaths
                        boolean isDuplicate = false;
                        for (Path existingPath : neighborPaths) {
                            if (existingPath.edges.equals(newPath.edges)) {
                                isDuplicate = true;
                                break;
                            }
                        }

                        // Если путь не дубликат и (neighborPaths.size() < K или новый путь лучше)
                        if (!isDuplicate && (neighborPaths.size() < K || arrivalTime.isBefore(neighborPaths.get(neighborPaths.size() - 1).time))) {
                            // Добавляем новый путь и сортируем список
                            neighborPaths.add(newPath);
                            neighborPaths.sort(Comparator.comparing(p -> p.time));

                            // Оставляем только K лучших путей
                            if (neighborPaths.size() > K) {
                                neighborPaths = new ArrayList<>(neighborPaths.subList(0, K));
                            }

                            // Обновляем список путей для вершины
                            arrivalPaths.put(neighbor, neighborPaths);

                            // Добавляем вершину в очередь
                            queue.add(new NodeTime(neighbor, arrivalTime));
                        }
                    }
                }
            }
        }

        return result;
    }
}