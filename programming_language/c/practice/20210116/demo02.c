#include <stdio.h>

// 函数外定义变量x、y
int x;
int y;

int add2num()
{
	extern int x;
	extern int y;
	
	x = 1;
	y = 2;
	return x + y;
}

int main()
{
	int result;
	result = add2num();

	printf("result 为: %d\n",result);
	return 0;
}
