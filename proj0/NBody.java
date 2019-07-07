public class NBody {
	public static void main(String[] args) {
		/* Reading Everything from Files */
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		Body[] bodies = readBodies(filename);
		double radius = readRadius(filename);
		/* Setting the scale so it matches radius */
		StdDraw.setScale(-radius, radius);
		/* drawing starfield.jpg as the background */
		StdDraw.picture(0, 0, "images/starfield.jpg");
		/* Drawing all the bodies */
		for (Body b : bodies) {
			b.draw();
		}
		StdDraw.enableDoubleBuffering();
		double time = 0;
		while (time <= T) {
			double[] xForces = new double[bodies.length];
			double[] yForces = new double[bodies.length];
			/* Calculating the net x and y forces for each Body */
			for (int i = 0; i < bodies.length; i++) {
				xForces[i] = bodies[i].calcNetForceExertedByX(bodies);
				yForces[i] = bodies[i].calcNetForceExertedByY(bodies);
			}
			/* Updating each Body */
			for (int i = 0; i < bodies.length; i++) {
				bodies[i].update(dt, xForces[i], yForces[i]);
			}
			/* Drawing background img */
			StdDraw.picture(0, 0, "images/starfield.jpg");
			/* Drawing all the bodies */
			for (Body b : bodies) {
				b.draw();
			}
			/* Showing offscreen buffer */
			StdDraw.show();
			/* Pausing the animation */
			StdDraw.pause(10);
			time += dt;
		}
		StdOut.printf("%d\n", bodies.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < bodies.length; i++) {
   	 		StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  bodies[i].xxPos, bodies[i].yyPos, bodies[i].xxVel,
                  bodies[i].yyVel, bodies[i].mass, bodies[i].imgFileName);   
		}
	}

	public static double readRadius(String file) {
		In in = new In(file);
		in.readInt();
		return in.readDouble();
	}

	public static Body[] readBodies(String file) {
		Body[] cont;
		In in = new In(file);
		int size = in.readInt();
		cont = new Body[size];
		in.readDouble();
		int index = 0;
		for (int i = 0; i < size; i++) {
			double xxPos = in.readDouble();
			double yyPos = in.readDouble();
			double xxVel = in.readDouble();
			double yyVel = in.readDouble();
			double mass = in.readDouble();
			String img = in.readString();
			cont[i] = new Body(xxPos, yyPos, xxVel, yyVel, mass, img);
		}
		return cont;
	}
}