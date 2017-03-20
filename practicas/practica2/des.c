#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "des.h"

//get bit by position
unsigned char getBit(unsigned char *elements, int idx){
    int i = idx / 8;
    int b = 1 << 7;
    b >>= (idx % 8);
    b &= elements[i];
    if(b == 0)
	return 0;
    return 1;
}

//set bit by postion
void setBit(unsigned char *elements, int idx, unsigned char bit){
    if(getBit(elements, idx) == bit)
	return;
    int i = idx / 8;
    int m = 1 << 7;
    m >>= (idx % 8);
    if(getBit(elements, idx) == 0)
	elements[i] |= m;
    else
        elements[i] ^= m;
}

/*Auxiliar, recibe tamaño en bits*/
void imprime(unsigned char *s, int size) {
    int i;
    for(i = 0; i < size; i++) 
        printf("%d", (int)getBit(s,i));
    printf("%c", '\n');
}

//apply function of each element
unsigned char* applyFunction(unsigned char (*f)(unsigned char,unsigned char), unsigned char *l_n, unsigned char *r_n, int size ){
    int idx;
    unsigned char *result = (unsigned char *) malloc (sizeof(unsigned char)*size);
    unsigned char resultFunction ;
    for(idx = 0; idx < size; idx ++) {
        resultFunction = (*f)(getBit(l_n, idx), getBit(r_n, idx));
        //The result of function is 1, 0 because we work with bits
        resultFunction &=1;
        setBit(result, idx, resultFunction);
    }
    return result;
}

//Permutation
unsigned char* applyPermutation(unsigned char *elements, int *permutation, int size){
    int idx;
    unsigned char *result = (unsigned char *) malloc(sizeof(unsigned char)*size);
    unsigned char resultFunction ;
    for (idx = 0; idx < size; idx++){
        resultFunction = getBit(elements, permutation[idx]-1); //-1 because the matrix idx begin i 1
        //The result of permutation is 1, 0 because we work with bits
        //resultFunction &=1;
        setBit(result, idx, resultFunction);
    }
    return result;
}

//Padding a key
unsigned char* padding(unsigned char* strK) {
    unsigned char * newKey =(unsigned char *) calloc(8,sizeof(unsigned char));
    int i;

    for(i = 0; i < strlen((char*)strK); i++) newKey[i] = strK[i];

    return newKey;
}

unsigned char* l_rotation(unsigned char *elements, int shift, int size){ //size in bits   
    unsigned char *result = (unsigned char *) calloc(size/sizeof(unsigned char),sizeof(unsigned char));
    int i, j;
    for(i = 0; i < size - shift; i++)
        setBit(result, i, getBit(elements, i + shift));
    for(i = size - shift, j = 0; i < size; i++, j++)
        setBit(result, i, getBit(elements, j));
    return result;
}

unsigned char** splitKeys (unsigned char *strk) {
    unsigned char **keys = (unsigned char **) malloc (sizeof(unsigned char*)*DES_ITERATIONS);//Number of keys
    unsigned char *key = applyPermutation(strk, pc1, 56);
    
    return keys;
}

unsigned char xorFunction(unsigned char a, unsigned char b){
    return a^b;
}

//Applies an specific S box
void applySi(unsigned char* input,int i, int* Si, unsigned char* output);
//Applies S box
unsigned char* applyS(unsigned char* elements){
    unsigned char *result = (unsigned char *) calloc(4,sizeof(unsigned char));
    //complete
    return result;
}

//DES encryption (1 block)
unsigned char * encryption(unsigned char *elements, unsigned char* key); //return cipher text

//Read file and normalize
unsigned char * encryptionFile(unsigned char *file_name, unsigned char* strK) {
    FILE *fp, *fw;
    int ch;//read character as an int
    fp = fopen((char *)file_name,"r"); // read mode
    fw = fopen("cipher_text.txt", "wb");//write mode
    if( fp == NULL || fw == NULL)  {
        perror("Error while opening the file.\n");
        exit(EXIT_FAILURE);
    }
    //complete
    fclose(fp);
    fclose(fw);
}
//DES decrypt (1 block)
unsigned char * decrypt(unsigned char* elements , unsigned char *key);

//Read file and normalize
unsigned char * decryptFile(unsigned char *file_name, unsigned char* strK) {
    FILE *fp, *fw;
    int counter;
    int ch; //read character as an int
    fp = fopen((char *)file_name,"rb"); // read mode
    fw = fopen("plain_text.txt", "w");//write mode
    if( fp == NULL || fw == NULL)  {
        perror("Error while opening the file.\n");
        exit(EXIT_FAILURE);
    }
    //complete
    fclose(fp);
    fclose(fw);
}
4
int main(int argc, char **args) {
    unsigned char* key = (unsigned char*)args[1];
    encryptionFile((unsigned char*)"file.txt", key);
    decryptFile((unsigned char*)"file.txt", key);
    /* unsigned char uc[7] = {-1,3,4,1,4,3,4}; */
    /* imprime(uc,7*8); */
    /* imprime(l_rotation(uc, 3, 7*8), 7*8); */
    return 0;
}
