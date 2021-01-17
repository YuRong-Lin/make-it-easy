#include <stdio.h>

const int y = 1;
int main()
{
	const int LENGTH = 10;
	const int WIDTH = 5;
	const char NEWLINE = '\n';
	int area;

	area = LENGTH * WIDTH;
	printf("value of area : %d", area);
	printf("%c %d\n", NEWLINE, y);

	return 0;
}
