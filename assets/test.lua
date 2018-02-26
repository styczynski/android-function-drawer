
function onFunctionLooking(x, y)
	return("@ = " .. x .. ", " .. y );
end

fd.setColor("black");

step = fd.getSettings().graphingStep;
print("WTFX = " .. fd.getCoord().left .. ", " .. fd.getCoord().right);
for x=fd.getCoord().left, fd.getCoord().right, step do
	fd.drawLine( x-step, x-step, x, x );
end
