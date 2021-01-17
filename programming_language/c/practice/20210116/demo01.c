#include <stdio.h>
#include <float.h>
#include <limits.h>

int main() {
	printf("hello, World! \n");
	// %lu为32位无符号整数 %E为指数形式输出单、双精度实数
	printf("int 存储大小 : %lu \n", sizeof(int));
	printf("float 存储最大字节数 : %lu \n", sizeof(float));
	printf("float 最小值: %E\n", FLT_MIN );
  	printf("float 最大值: %E\n", FLT_MAX );
  	printf("精度值: %d\n", FLT_DIG );	
	return 0;
}

/* clang demo01.c -o demo01.out 
   gcc demo01.c -o demo01.out
*/
