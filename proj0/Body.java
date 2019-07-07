public class Body {
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;
	public static final double G = 6.67e-11;

	public Body(double xxPos, double yyPos, double xxVel,
		double yyVel, double mass, String imgFileName) {
		this.xxPos = xxPos;
		this.yyPos = yyPos;
		this.xxVel = xxVel;
		this.yyVel = yyVel;
		this.mass = mass;
		this.imgFileName = imgFileName;
	}

	public Body(Body b) {
		this.xxPos = b.xxPos;
		this.yyPos = b.yyPos;
		this.xxVel = b.xxVel;
		this.yyVel = b.yyVel;
		this.mass = b.mass;
		this.imgFileName = b.imgFileName;
	}

	public double calcDistance(Body b) {
		double dx = this.xxPos - b.xxPos;
		double dy = this.yyPos - b.yyPos;
		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
	}

	public double calcForceExertedBy(Body b) {
		double sqrdDistance = Math.pow(this.calcDistance(b), 2);
		return Body.G * this.mass * b.mass / sqrdDistance;
	}

	public double calcForceExertedByX(Body b) {
		double dx = b.xxPos - this.xxPos;
		double distance = this.calcDistance(b);
		double forceExtertedBy = this.calcForceExertedBy(b);
		return forceExtertedBy * dx / distance;
	}

	public double calcForceExertedByY(Body b) {
		double dy = b.yyPos - this.yyPos;
		double distance = this.calcDistance(b);
		double forceExtertedBy = this.calcForceExertedBy(b);
		return forceExtertedBy * dy / distance;
	}

	public double calcNetForceExertedByX(Body[] allBodys) {
		double netForce = 0;
		for (Body b : allBodys) {
			if (!this.equals(b)) {
				netForce += this.calcForceExertedByX(b);
			}
		}
		return netForce;
	}

	public double calcNetForceExertedByY(Body[] allBodys) {
		double netForce = 0;
		for (Body b : allBodys) {
			if (!this.equals(b)) {
				netForce += this.calcForceExertedByY(b);
			}
		}
		return netForce;
	}

	public void update(double dt, double fX, double fY) {
		double accX = fX / this.mass;
		double accY = fY / this.mass;
		this.xxVel += dt * accX;
		this.yyVel += dt * accY;
		this.xxPos += dt * this.xxVel;
		this.yyPos += dt * this.yyVel;
	}
}
