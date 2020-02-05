 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw5;

import java.io.File;
import java.io.FileNotFoundException;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Scanner;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdRandom;
import java.awt.Color;

public class Main {
    
    public static class Particle {
        private static final double INFINITY = Double.POSITIVE_INFINITY;

        private double rx, ry;        // position
        private double vx, vy;        // velocity
        private int count;            // number of collisions so far
        private final double radius;  // radius
        private final double mass;    // mass
        private final Color color;    // color


        /**
         * Initializes a particle with the specified position, velocity, radius, mass, and color.
         *
         * @param  rx <em>x</em>-coordinate of position
         * @param  ry <em>y</em>-coordinate of position
         * @param  vx <em>x</em>-coordinate of velocity
         * @param  vy <em>y</em>-coordinate of velocity
         * @param  radius the radius
         * @param  mass the mass
         * @param  color the color
         */
        public Particle(double rx, double ry, double vx, double vy, double radius, double mass, Color color) {
            this.vx = vx;
            this.vy = vy;
            this.rx = rx;
            this.ry = ry;
            this.radius = radius;
            this.mass   = mass;
            this.color  = color;
        }

        /**
         * Initializes a particle with a random position and velocity.
         * The position is uniform in the unit box; the velocity in
         * either direciton is chosen uniformly at random.
         */
        public Particle() {
            rx     = StdRandom.uniform(0.0, 1.0);
            ry     = StdRandom.uniform(0.0, 1.0);
            vx     = StdRandom.uniform(-0.005, 0.005);
            vy     = StdRandom.uniform(-0.005, 0.005);
            radius = 0.02;
            mass   = 0.5;
            color  = Color.BLACK;
        }

        /**
         * Moves this particle in a straight line (based on its velocity)
         * for the specified amount of time.
         *
         * @param  dt the amount of time
         */
        public void move(double dt) {
            rx += vx * dt;
            ry += vy * dt;
        }

        /**
         * Draws this particle to standard draw.
         */
        public void draw() {
            StdDraw.setPenColor(color);
            StdDraw.filledCircle(rx, ry, radius);
        }

        /**
         * Returns the number of collisions involving this particle with
         * vertical walls, horizontal walls, or other particles.
         * This is equal to the number of calls to {@link #bounceOff},
         * {@link #bounceOffVerticalWall}, and
         * {@link #bounceOffHorizontalWall}.
         *
         * @return the number of collisions involving this particle with
         *         vertical walls, horizontal walls, or other particles
         */
        public int count() {
            return count;
        }

        /**
         * Returns the amount of time for this particle to collide with the specified
         * particle, assuming no interening collisions.
         *
         * @param  that the other particle
         * @return the amount of time for this particle to collide with the specified
         *         particle, assuming no interening collisions; 
         *         {@code Double.POSITIVE_INFINITY} if the particles will not collide
         */
        public double timeToHit(Particle that) {
            if (this == that) return INFINITY;
            double dx  = that.rx - this.rx;
            double dy  = that.ry - this.ry;
            double dvx = that.vx - this.vx;
            double dvy = that.vy - this.vy;
            double dvdr = dx*dvx + dy*dvy;
            if (dvdr > 0) return INFINITY;
            double dvdv = dvx*dvx + dvy*dvy;
            if (dvdv == 0) return INFINITY;
            double drdr = dx*dx + dy*dy;
            double sigma = this.radius + that.radius;
            double d = (dvdr*dvdr) - dvdv * (drdr - sigma*sigma);
            // if (drdr < sigma*sigma) StdOut.println("overlapping particles");
            if (d < 0) return INFINITY;
            return -(dvdr + Math.sqrt(d)) / dvdv;
        }
        
        public double timeToCorner(double rx, double ry) {
            
            double dx  = rx - this.rx;
            double dy  = ry - this.ry;
            double dvx =  - this.vx;
            double dvy =  - this.vy;
            double dvdr = dx*dvx + dy*dvy;
            if (dvdr > 0) return INFINITY;
            double dvdv = dvx*dvx + dvy*dvy;
            if (dvdv == 0) return INFINITY;
            double drdr = dx*dx + dy*dy;
            double sigma = this.radius ;
            double d = (dvdr*dvdr) - dvdv * (drdr - sigma*sigma);
            // if (drdr < sigma*sigma) StdOut.println("overlapping particles");
            if (d < 0) return INFINITY;
            return -(dvdr + Math.sqrt(d)) / dvdv;
        }
        
        public double timeToHitVerticalWall() {
            if      (vx > 0) return (1.0 - rx - radius) / vx;
            else if (vx < 0) return (radius - rx) / vx;  
            else             return INFINITY;
        }

        public double timeToHitHorizontalWall() {
            if      (vy > 0) return (1.0 - ry - radius) / vy;
            else if (vy < 0) return (radius - ry) / vy;
            else             return INFINITY;
        }
        
        public double timeToHitVerticalSqr() {
            if      (vx > 0) {
                if (rx <= 0.4 + radius)  return HitXEdge(0.4 - radius - rx);
                else                    return INFINITY;
            }
            else if (vx < 0) {
                if (rx >= 0.6 + radius)  return HitXEdge(0.6 + radius - rx);
                else                    return INFINITY;
            }    
            else    return INFINITY;
        }
        
        public double timeToHitHorizontalSqr() {
            if      (vy > 0) {
                if (ry <= 0.4 + radius)  return HitYEdge(0.4 - radius - ry);
            }
            else if (vy < 0) {
                if (ry >= 0.6 + radius)  return HitYEdge(0.6 + radius - ry);
            }    
            return INFINITY;
        }
        
        public double HitXEdge(double dx) {
            double t = dx / vx ;
            double yy = ry + t * vy;
            if (yy >= 0.4 && yy <= 0.6)   return t ;
            else if (yy >= 0.4 - radius && yy < 0.4){ 
                if (rx < 0.4)      return timeToCorner(0.4, 0.4);
                else if (rx > 0.6) return timeToCorner(0.6, 0.4);
                else return INFINITY;
                }
            else if (yy <= 0.6 +  radius && yy > 0.6) { 
                if (rx < 0.4)      return timeToCorner(0.4, 0.6);
                else if (rx > 0.6) return timeToCorner(0.6, 0.6);
                else return INFINITY;
                }
            else              return INFINITY;
        }
        
        public double HitYEdge(double dy) {
            double t = dy / vy ;
            double xx = rx + t * vx;
            if (xx >= 0.4 && xx <= 0.6)   return t ;
            else if (xx >= 0.4 - radius && xx < 0.4){ 
                if (ry < 0.4)      return timeToCorner(0.4, 0.4);
                else if (ry > 0.6) return timeToCorner(0.4, 0.6);
                else return INFINITY;
                }
            else if (xx <= 0.6 +  radius && xx > 0.6) { 
                if (ry < 0.4)       return timeToCorner(0.6, 0.4);
                else if (ry > 0.6)  return timeToCorner(0.6, 0.6);
                else return INFINITY;
                }
            else              return INFINITY;
        }
        
       
        public void bounceOff(Particle that) {
            double dx  = that.rx - this.rx;
            double dy  = that.ry - this.ry;
            double dvx = that.vx - this.vx;
            double dvy = that.vy - this.vy;
            double dvdr = dx*dvx + dy*dvy;             // dv dot dr
            double dist = this.radius + that.radius;   // distance between particle centers at collison

            // magnitude of normal force
            double magnitude = 2 * this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist);

            // normal force, and in x and y directions
            double fx = magnitude * dx / dist;
            double fy = magnitude * dy / dist;

            // update velocities according to normal force
            this.vx += fx / this.mass;
            this.vy += fy / this.mass;
            that.vx -= fx / that.mass;
            that.vy -= fy / that.mass;

            // update collision counts
            this.count++;
            that.count++;
        }
        

        /**
         * Updates the velocity of this particle upon collision with a vertical
         * wall (by reflecting the velocity in the <em>x</em>-direction).
         * Assumes that the particle is colliding with a vertical wall at this instant.
         */
        public void bounceOffVerticalWall() {
            vx = -vx;
            count++;
        }

        /**
         * Updates the velocity of this particle upon collision with a horizontal
         * wall (by reflecting the velocity in the <em>y</em>-direction).
         * Assumes that the particle is colliding with a horizontal wall at this instant.
         */
        public void bounceOffHorizontalWall() {
            vy = -vy;
            count++;
        }

        /**
         * Returns the kinetic energy of this particle.
         * The kinetic energy is given by the formula 1/2 <em>m</em> <em>v</em><sup>2</sup>,
         * where <em>m</em> is the mass of this particle and <em>v</em> is its velocity.
         *
         * @return the kinetic energy of this particle
         */
        public double kineticEnergy() {
            return 0.5 * mass * (vx*vx + vy*vy);
        }
    }

    public static class ColliSystem {
        private static final double HZ = 0.5;    // number of redraw events per clock tick

        private MinPQ<Event> pq;          // the priority queue
        private double t  = 0.0;          // simulation clock time
        private Particle[] particles;     // the array of particles
        private boolean test;
        private int[] counts;
        
        /**
         * Initializes a system with the specified collection of particles.
         * The individual particles will be mutated during the simulation.
         *
         * @param  particles the array of particles
         * @param  test check whether to draw
         */
        public ColliSystem(Particle[] particles, boolean test) {
            this.particles = particles.clone();   // defensive copy
            this.test = test;
            this.counts = new int[particles.length];
            
        }

        // updates priority queue with all new events for particle a
        private void predict(Particle a, double limit) {
            if (a == null) return;

            // particle-particle collisions
            for (int i = 0; i < particles.length; i++) {
                double dt = a.timeToHit(particles[i]);
                if (t + dt <= limit)
                    pq.insert(new Event(t + dt, a, particles[i]));
            }
            
            // particle-wall collisions
            double dtX = a.timeToHitVerticalWall();
            double dtY = a.timeToHitHorizontalWall();
            if (t + dtX <= limit) pq.insert(new Event(t + dtX, a, null));
            if (t + dtY <= limit) pq.insert(new Event(t + dtY, null, a));
            
            // particle-square collisions
            double dtXQ = a.timeToHitVerticalSqr();
            double dtYQ = a.timeToHitHorizontalSqr();
            if (t + dtXQ <= limit) pq.insert(new Event(t + dtXQ, a, null));
            if (t + dtYQ <= limit) pq.insert(new Event(t + dtYQ, null, a));
          
        }

        // redraw all particles
        private void redraw(double limit) {
            if (test){
                StdDraw.clear();
                for (int i = 0; i < particles.length; i++) {
                    particles[i].draw();
                }

                StdDraw.filledSquare(0.5, 0.5, 0.1);
                StdDraw.show();
                StdDraw.pause(20);
            }
            if (t < limit) {
                pq.insert(new Event(t + 1.0 / HZ, null, null));
            }
        }

        public int[] BounceCount() {
            for (int i = 0; i < particles.length; i++) {
                counts[i] =  particles[i].count();
            }
            return counts;
        }
        /**
         * Simulates the system of particles for the specified amount of time.
         *
         * @param  limit the amount of time
         */
        public void simulate(double limit) {

            // initialize PQ with collision events and redraw event
            pq = new MinPQ<Event>();
            for (int i = 0; i < particles.length; i++) {
                predict(particles[i], limit);
            }
            pq.insert(new Event(0, null, null));        // redraw event


            // the main event-driven simulation loop
            while (!pq.isEmpty()) { 

                // get impending event, discard if invalidated
                Event e = pq.delMin();
                if (!e.isValid()) continue;
                Particle a = e.a;
                Particle b = e.b;

                // physical collision, so update positions, and then simulation clock
                for (int i = 0; i < particles.length; i++)
                    particles[i].move(e.time - t);
                t = e.time;
                boolean bc = false;
                // process event
                if      (a != null && b != null) a.bounceOff(b);// particle-particle collision
                else if (a != null && b == null) a.bounceOffVerticalWall();   // particle-wall collision
                else if (a == null && b != null) b.bounceOffHorizontalWall(); // particle-wall collision
                else if (a == null && b == null) redraw(limit);               // redraw event

                // update the priority queue with new collisions involving a or b
                predict(a, limit);
                predict(b, limit);
            }
        }


   /***************************************************************************
    *  An event during a particle collision simulation. Each event contains
    *  the time at which it will occur (assuming no supervening actions)
    *  and the particles a and b involved.
    *
    *    -  a and b both null:      redraw event
    *    -  a null, b not null:     collision with vertical wall
    *    -  a not null, b null:     collision with horizontal wall
    *    -  a and b both not null:  binary collision between a and b
    *
    ***************************************************************************/
        private class Event implements Comparable<Event> {
            private final double time;         // time that event is scheduled to occur
            private final Particle a, b;       // particles involved in event, possibly null
            private final int countA, countB;  // collision counts at event creation


            // create a new event to occur at time t involving a and b
            public Event(double t, Particle a, Particle b) {
                this.time = t;
                this.a    = a;
                this.b    = b;
                if (a != null) countA = a.count();
                else           countA = -1;
                if (b != null) countB = b.count();
                else           countB = -1;
            }

            // compare times when two events will occur
            public int compareTo(Event that) {
                return Double.compare(this.time, that.time);
            }

            // has any collision occurred between when event was created and now?
            public boolean isValid() {
                if (a != null && a.count() != countA) return false;
                if (b != null && b.count() != countB) return false;
                return true;
            }

        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        boolean test = true;
        File file;
        
        test = false;
        
        if (test) 
            file = new File("test.in") ; // filename for local test (delete this part when uploading to onlinejudge)
        else
            file = new File(args[0]) ;// file name assigned
        
        Scanner in = new Scanner(file);
        
        int N = Integer.parseInt(in.nextLine());
        int T = Integer.parseInt(in.nextLine());
        double rx, ry, vx, vy, r, m;
        String[] party = new String[6];
        
        if (test) {
            StdDraw.setCanvasSize(600, 600);
            StdDraw.enableDoubleBuffering();
        }
        
        Particle[] parties = new Particle[N];
        int[] bounce = new int[N];
        
        for (int i = 0; i < N ; i++) {
            party = in.nextLine().split(" ");
            rx = Double.parseDouble(party[0]);
            ry = Double.parseDouble(party[1]);
            vx = Double.parseDouble(party[2]);
            vy = Double.parseDouble(party[3]);
            r =  Double.parseDouble(party[4]);
            m  = Double.parseDouble(party[5]);
            parties[i] = new Particle(rx, ry, vx, vy, r, m, Color.BLACK);
        }
        
        ColliSystem system = new ColliSystem(parties, test);
        system.simulate(T);
        bounce = system.BounceCount();
        for (int i = 0; i < N ; i++)
            System.out.println(bounce[i]);
        
    }
    
}
