package com.example.extremetic_tac_toe;

public class CircleLayout extends RelativeLayout {
    static final int centerId = 111;
    private final int radius;

    public CircleLayout(final android.content.Context context, final android.util.AttributeSet attr) {
	// super(context, attr);
	this(context, 50, new View[] {});
    }

    public CircleLayout(final Context context, final int radius, final View[] elements) {
	super(context);
	this.radius = radius;

	final RelativeLayout.LayoutParams lpView = new RelativeLayout.LayoutParams(
		RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
	this.setLayoutParams(lpView);

	final View center = new View(context);
	center.setId(CircleLayout.centerId);
	final RelativeLayout.LayoutParams lpcenter = new RelativeLayout.LayoutParams(0, 0);
	lpcenter.addRule(CENTER_HORIZONTAL);
	lpcenter.addRule(CENTER_VERTICAL);
	center.setLayoutParams(lpcenter);
	this.addView(center);

	this.addView(this.prepareElementForCircle(elements[0], 0, 0));
	if (elements.length % 2 == 0) {
	    this.addView(this.prepareElementForCircle(elements[elements.length / 2], 0, 2 * radius));
	}
	if (elements.length > 2) {
	    for (int i = 1; i <= (elements.length - 1) / 2; i++) {
		final int y = i * 4 * radius / elements.length;
		final int x = (int) Math.sqrt(Math.pow(radius, 2) - Math.pow(radius - y, 2));
		this.addView(this.prepareElementForCircle(elements[i], x, y));
		this.addView(this.prepareElementForCircle(elements[elements.length - i], -x, y));
	    }
	}
    }

    private RelativeLayout.LayoutParams createNewRelativeLayoutParams() {
	final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
		RelativeLayout.LayoutParams.WRAP_CONTENT);
	lp.addRule(RelativeLayout.ABOVE, CircleLayout.centerId);
	lp.addRule(RIGHT_OF, CircleLayout.centerId);
	return lp;
    }

    private View prepareElementForCircle(final View elem, final int distX, final int distY) {
	final RelativeLayout.LayoutParams lp = this.createNewRelativeLayoutParams();

	elem.measure(0, 0);
	final int deltaX = elem.getMeasuredWidth() / 2;
	final int deltaY = elem.getMeasuredHeight() / 2;
	lp.setMargins(distX - deltaX, 0, 0, this.radius - distY - deltaY);
	elem.setLayoutParams(lp);
	return elem;
    }

}
