package org.kelvinizer.display;

import org.kelvinizer.shapes.COval;

import java.util.PriorityQueue;

import static org.kelvinizer.params.BorderParams.*;
import static org.kelvinizer.params.ColorParams.routerPlacedColor;
import static org.kelvinizer.params.ObjectDimensionParams.*;

public class Router extends COval {

    private double initialDBM;

    private double wallLoss;

    public final double[][] signal =
            new double[BORDER_SIZE][BORDER_SIZE];

    public static final int CHUNK_SIZE = 6;

    public Router(double x, double y, double txPowerDbm, double wallLossDb) {
        super(ROUTER_SIZE, ROUTER_SIZE);
        setFillColor(routerPlacedColor);
        setPosition(x, y);
        this.initialDBM = txPowerDbm;
        this.wallLoss = wallLossDb;
        recalibrateSignalStrength();
    }

    public Router(double txPowerDbm, double wallLossDb) {
        this(BORDER_CENTER_X, BORDER_CENTER_Y, txPowerDbm, wallLossDb);
    }

    public Router(){
        this(DEFAULT_INITIAL_DBM, DEFAULT_WALL_LOSS);
    }

    public void recalibrateSignalStrength() {
        recalibrateSignalStrength(
                (int) getX() + BORDER_SIZE / 2 - BORDER_CENTER_X,
                (int) getY() + BORDER_SIZE / 2 - BORDER_CENTER_Y
        );
    }

    public void recalibrateSignalStrength(int sx, int sy) {
        for (int x = 0; x < BORDER_SIZE; x++) {
            for (int y = 0; y < BORDER_SIZE; y++) {
                signal[x][y] = Double.NEGATIVE_INFINITY;
            }
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        signal[sx][sy] = initialDBM;
        pq.add(new Node(sx, sy, initialDBM));

        while (!pq.isEmpty()) {
            Node cur = pq.poll();

            if (cur.dbm < signal[cur.x][cur.y]) continue;

            for (int[] d : DIRS) {
                int nx = cur.x + d[0];
                int ny = cur.y + d[1];

                if (nx < 0 || ny < 0 ||
                        nx > BORDER_SIZE - 1 ||
                        ny > BORDER_SIZE - 1)
                    continue;

                double loss = AIR_LOSS_PER_UNIT;

                // 墙体损耗
                if (Display.wallMask[nx][ny]) {
                    loss += wallLoss;
                }

                double nextDbm = cur.dbm - loss * Math.sqrt(d[0] * d[0] + d[1] * d[1]);
                if (nextDbm > signal[nx][ny]) {
                    signal[nx][ny] = nextDbm;
                    pq.add(new Node(nx, ny, nextDbm));
                }
            }
        }
    }

    private static final int[][] DIRS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1}
    };

    private static class Node implements Comparable<Node> {
        int x, y;
        double dbm;

        Node(int x, int y, double dbm) {
            this.x = x;
            this.y = y;
            this.dbm = dbm;
        }

        @Override
        public int compareTo(Node o) {
            return Double.compare(o.dbm, this.dbm); // max-heap
        }
    }

    public double getInitialDBM() {
        return initialDBM;
    }

    public void setInitialDBM(double initialDBM) {
        this.initialDBM = initialDBM;
        recalibrateSignalStrength();
    }

    public double getWallLoss() {
        return wallLoss;
    }

    public void setWallLoss(double wallLoss) {
        this.wallLoss = wallLoss;
        recalibrateSignalStrength();
    }

    @Override
    public Router clone() {
        Router r = new Router(getX(), getY(), initialDBM, wallLoss);
        r.setFillColor(getFillColor());
        return r;
    }
}