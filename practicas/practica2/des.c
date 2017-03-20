#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include "des.h"
#include <math.h>

//get bit by position
unsigned char getBit(unsigned char *elements, int idx){
    int i = idx / CHAR_BIT;
    int b = 1 << (CHAR_BIT - 1);
    b >>= (idx % CHAR_BIT);
    b &= elements[i];
    if(b == 0)
	return 0;
    return 1;
}

//set bit by postion
void setBit(unsigned char *elements, int idx, unsigned char bit){
    if(getBit(elements, idx) == bit)
	return;
    int i = idx / CHAR_BIT;
    int m = 1 << (CHAR_BIT - 1);
    m >>= (idx % CHAR_BIT);
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

/*Auxiliar, imprime todas las llaves, sizeks es núm de llaves, sizek es tamaño de cada llave*/
void imprime_ks(unsigned char **ks, int sizeks, int sizek) {
    int i;
    for(i = 0; i < sizeks; i++)
        imprime(ks[i], sizek);
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

//Permutation, size in bits
unsigned char* applyPermutation(unsigned char *elements, int *permutation, int size){
    int idx;
    unsigned char *result = (unsigned char *) malloc(ceil((double)size / (double)CHAR_BIT));
    unsigned char resultFunction ;
    for (idx = 0; idx < size; idx++){
        resultFunction = getBit(elements, permutation[idx]-1); //-1 because the matrix idx begin i 1
        //The result of permutation is 1, 0 because we work with bits
        //resultFunction &=1;
        setBit(result, idx, resultFunction);
    }
    return result;
}

//Padding a key (WATS TIS SHIT)
/*
unsigned char* padding(unsigned char* strK) {
    unsigned char * newKey =(unsigned char *) calloc(8,sizeof(unsigned char));
    int i;

    for(i = 0; i < strlen((char*)strK); i++) newKey[i] = strK[i];

    return newKey;
}
*/

// Shift a la izquierda
unsigned char* l_rotation(unsigned char *elements, int shift, int size){ //size in bits   
    unsigned char *result = (unsigned char *) calloc(size/CHAR_BIT, sizeof(unsigned char));
    int i, j;
    for(i = 0; i < size - shift; i++)
        setBit(result, i, getBit(elements, i + shift));
    for(i = size - shift, j = 0; i < size; i++, j++)
        setBit(result, i, getBit(elements, j));
    return result;
}

// Regresa la subcadena del indice b al indice e, i.e. [b, e), índices de los bits.
unsigned char* substr(unsigned char *str, int b, int e) {
    int size = ceil((double)(e - b) / (double)CHAR_BIT);
    unsigned char *result = (unsigned char *) malloc(size);
    int i;
    for(i = 0; i < e - b; i++)
        setBit(result, i, getBit(str, i + b));
    return result;
}

// Regresa la concatenación de dos cadenas, recibe tamaños de cadenas en bits
unsigned char* catstr(unsigned char *str1, unsigned char *str2, int size1, int size2) {
    unsigned char *result = (unsigned char *) malloc(ceil((double)(size1 + size2) / (double)CHAR_BIT));
    int i, j;
    for(i = 0; i < size1; i++)
        setBit(result, i, getBit(str1, i));
    for(i = size1, j = 0; i < size1 + size2; i++, j++)
        setBit(result, i, getBit(str2, j));
    return result;
}

// Libera la memoria que ocupan las llaves
void free_keys(unsigned char **ks) {
    int i;
    for(i = 0; i < DES_ITERATIONS; i++)
        free(ks[i]);
    free(ks);
}

//Genera las 16 llaves a partir de la llave principal
unsigned char** splitKeys (unsigned char *strk) {
    unsigned char **keys = (unsigned char **) malloc (sizeof(unsigned char*)*DES_ITERATIONS);//Number of keys1
    unsigned char *key = applyPermutation(strk, pc1, 56);
    unsigned char *c = substr(key, 0, 28);
    unsigned char *d = substr(key, 28, 56);
    free(key);
    int i;
    for(i = 0; i < DES_ITERATIONS; i++) {
        unsigned char *c1 = l_rotation(c, noShift[i], 28);
        unsigned char *d1 = l_rotation(d, noShift[i], 28);
        free(c);
        free(d);
        unsigned char *cd = catstr(c1, d1, 28, 28);
        keys[i] = applyPermutation(cd, pc2, 48);
        c = substr(cd, 0, 28);
        d = substr(cd, 28, 56);
        free(cd);
        free(c1);
        free(d1);
    }
    free(c);
    free(d);
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

int main(int argc, char **args) {
    /*
    unsigned char* key = (unsigned char*)args[1];
    encryptionFile((unsigned char*)"file.txt", key);
    decryptFile((unsigned char*)"file.txt", key);
    */
    
    /*** PRUEBAS ***/

    // Prueba left shift
    /*
    unsigned char uc[7] = {-1,3,4,1,4,3,4};
    imprime(uc,7*8);
    imprime(l_rotation(uc, 3, 7*8), 7*8);
    */

    // Prueba permutacion
    /*
    unsigned char uc[8] = {-1,3,4,1,4,3,4,12};
    unsigned char uc2[8] = {0,3,5,1,12,14,40,12};
    imprime(uc,8*8);
    imprime(uc2,8*8);
    unsigned char *perm = applyPermutation(uc, IP, 64);
    imprime(perm,8*8);
    free(perm);
    unsigned char *cat = catstr(uc, uc2, 64, 64);
    imprime(cat, 64+64);
    free(cat);
    */

    // Prueba generacion de llaves
    /*
    unsigned char k[8] = {-1,3,4,1,4,3,4,12};
    imprime(k, 64);
    unsigned char** ks =splitKeys(k);
    int i;
    imprime_ks(ks, DES_ITERATIONS, 48);
    free_keys(ks);
    */
    return 0;
}
