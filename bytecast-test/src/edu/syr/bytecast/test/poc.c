#include <stdio.h>

int main(int argc, char* argv[])
{
	if(argc > 0)
		printf("%s", argv[1]);
	else
		printf("default");
	
	return 0;
}
