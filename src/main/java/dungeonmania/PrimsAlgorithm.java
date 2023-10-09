package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONObject;
import org.json.JSONArray;
import dungeonmania.util.Position;

public class PrimsAlgorithm {
    public JSONObject randomisedPrims(int width, int height, Position start, Position end) {
        int leftBound = 0;
        int rightBound = 0;
        int upperBound = 0;
        int lowerBound = 0;
        boolean[][] map = new boolean[width][height];

        Position originalStart = start;
        Position originalEnd = end;

        start = new Position (1, 1);
        end = new Position (width - 2, height - 2);
        leftBound = start.getX() - 1;
        rightBound = end.getX() + 1;
        upperBound = start.getY() - 1;
        lowerBound = end.getY() + 1;
        for (int i = 0; i < width - 1; i++) {
            for (int j = 0; j < height - 1; j++) {
                map[i][j] = false;
            }
        }

        map[start.getX()][start.getY()] = true; 
        List<Position> options = new ArrayList<Position>();

        // Add to options all neighbours of 'start' not on boundary that are of distance 2 away and are walls
        if (start.getX() + 2 < rightBound && map[start.getX() + 2][start.getY()] == false) {
            Position p = new Position(start.getX() + 2, start.getY());
            options.add(p);
        }
        if (start.getX() - 2 > leftBound && map[start.getX() - 2][start.getY()] == false) {
            Position p = new Position(start.getX() - 2, start.getY());
            options.add(p);
        }
        if (start.getY() + 2 < lowerBound && map[start.getX()][start.getY() + 2] == false) {
            Position p = new Position(start.getX(), start.getY() + 2);
            options.add(p); 
        }
        if (start.getY() - 2 > upperBound && map[start.getX()][start.getY() - 2] == false) {
            Position p = new Position(start.getX(), start.getY() - 2);
            options.add(p);
        }

        // While options is not empty:
        while (options.size() != 0) {
            Position next = options.get(new Random().nextInt(options.size()));
            options.remove(next);
            List<Position> neighbours = new ArrayList<Position>();

            if (map[next.getX()][next.getY()] == false) {
                if (next.getX() + 2 < rightBound&& map[next.getX() + 2][next.getY()] == true) {
                    Position p = new Position(next.getX() + 2, next.getY());
                    neighbours.add(p);
                }
                if (next.getX() - 2 > leftBound && map[next.getX() - 2][next.getY()] == true) {
                    Position p = new Position(next.getX() - 2, next.getY());
                    neighbours.add(p);
                }
                if (next.getY() + 2 < lowerBound && map[next.getX()][next.getY() + 2] == true) {
                    Position p = new Position(next.getX(), next.getY() + 2);
                    neighbours.add(p); 
                }
                if (next.getY() - 2 > upperBound && map[next.getX()][next.getY() - 2] == true) {
                    Position p = new Position(next.getX(), next.getY() - 2);
                    neighbours.add(p);
                }
            }   

            // If neighbours is not empty:
            if (neighbours.size() != 0) {
                Position neighbour = neighbours.get(new Random().nextInt(neighbours.size()));
                if (neighbour.getX() == next.getX() + 2) {
                    map[next.getX() + 1][next.getY()] = true;
                }
                else if (neighbour.getX() == next.getX() - 2) {
                    map[next.getX() - 1][next.getY()] = true;
                }
                else if (neighbour.getY() == next.getY() + 2) {
                    map[next.getX()][next.getY() + 1] = true;
                }
                else if (neighbour.getY() == next.getY() - 2) {
                    map[next.getX()][next.getY() - 1] = true;
                }
                map[next.getX()][next.getY()] = true;
                map[neighbour.getX()][neighbour.getY()] = true;
            }

            if (next.getX() + 2 < rightBound && map[next.getX() + 2][next.getY()] == false) {
                Position p = new Position(next.getX() + 2, next.getY());
                options.add(p);
            }
            if (next.getX() - 2 > leftBound && map[next.getX() - 2][next.getY()] == false) {
                Position p = new Position(next.getX() - 2, next.getY());
                options.add(p);
            }
            if (next.getY() + 2 < lowerBound && map[next.getX()][next.getY() + 2] == false) {
                Position p = new Position(next.getX(), next.getY() + 2);
                options.add(p); 
            }
            if (next.getY() - 2 > upperBound && map[next.getX()][next.getY() - 2] == false) {
                Position p = new Position(next.getX(), next.getY() - 2);
                options.add(p);
            }
        }

        if (map[end.getX()][end.getY()] == false) {
            boolean isEmpty = false;
            map[end.getX()][end.getY()] = true;
            List<Position> endNeighbours = new ArrayList<Position>();
            if (end.getX() + 1 < rightBound) {
                Position p = new Position(end.getX() + 1, end.getY());
                endNeighbours.add(p);
            }
            if (end.getX() - 1 > leftBound) {
                Position p = new Position(end.getX() - 1, end.getY());
                endNeighbours.add(p);
            }
            if (end.getY() + 1 < lowerBound) {
                Position p = new Position(end.getX(), end.getY() + 1);
                endNeighbours.add(p); 
            }
            if (end.getY() - 1 > upperBound) {
                Position p = new Position(end.getX(), end.getY() - 1);
                endNeighbours.add(p);
            }

            for (Position p : endNeighbours) {
                if (map[p.getX()][p.getY()] == true) {
                    isEmpty = true;
                }
            }

            if (isEmpty == false ) { 
                Position random = endNeighbours.get(new Random().nextInt(endNeighbours.size()));
                map[random.getX()][random.getY()] = true;
            }
        }

        JSONObject o = new JSONObject();
        JSONArray entitiesList = new JSONArray();

        // Add exit and player to JSON
        JSONObject player = new JSONObject();
        player.put("type", "player");
        player.put("x", originalStart.getX());
        player.put("y", originalStart.getY());
        entitiesList.put(player);

        JSONObject exit = new JSONObject();
        exit.put("type", "exit");
        exit.put("x", originalEnd.getX());
        exit.put("y", originalEnd.getY());
        entitiesList.put(exit);

        int deltaX = originalStart.getX() - start.getX();
        int deltaY = originalStart.getY() - start.getY();

        // Create JSON array of walls, shift array to original starting position
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(map[i][j] == false) {
                    JSONObject newEntity = new JSONObject();
                    newEntity.put("type", "wall");
                    newEntity.put("x", i + deltaX);
                    newEntity.put("y", j + deltaY);
                    entitiesList.put(newEntity);
                }
            }
        }

        // Add entities and goals to final JSON object
        o.put("entities", entitiesList);
        JSONObject goalContent = new JSONObject();
        goalContent.put("goal", "exit");
        o.put("goal-condition", goalContent);
        return o;
    }
}
