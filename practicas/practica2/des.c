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

void imprime_n(unsigned char c) {
    int m = 1;
    m <<= 7;
    while(m > 0) {
        if((c&m) != 0)
            printf("%d", 1);
        else
            printf("%d", 0);
        m >>= 1;
    }
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

// Regresa la representación en char de los bits entre [b, e)
unsigned char getChar(unsigned char* elements, int b, int e) {
    int i, j, res = 0;
    for(i = e - 1, j = 0; i >= b; i--, j++)
        res |= getBit(elements, i) << j;
    return res;
}

int getCol(unsigned char c) {
    int i, mascara = 1 << 4, res = 0;
    for(i = 0; i < 4; i++) {
        res |= mascara & c;
        mascara >>= 1;
    }
    return res >> 1;
}

int getRow(unsigned char c) {
    int res = 0;
    if(((1 << 5) & c) != 0)
        res |= 2;
    res |= c & 1;
    return res;
}

//Applies an specific S box
// void applySi(unsigned char* input, int i, int* Si, unsigned char* output);
void applySi(unsigned char* elements, unsigned char* result, int i, int* Si) {
    unsigned char c = getChar(elements, (i - 1) * 6, (i - 1) * 6 + 6);
    int s = Si[getCol(c) + getRow(c) * 16];
   int j, m = 1 << 3;
    for(j = 0; j < 4; j++) {
        if((m & s) != 0)
            setBit(result, (i - 1) * 4 + j, 1);
        else
            setBit(result, (i - 1) * 4 + j, 0);
        m >>= 1;
    }
}

//Applies S box
unsigned char* applyS(unsigned char* elements){
    unsigned char *result = (unsigned char *) calloc(4,sizeof(unsigned char));
    applySi(elements, result, 1, S1);
    applySi(elements, result, 2, S2);
    applySi(elements, result, 3, S3);
    applySi(elements, result, 4, S4);
    applySi(elements, result, 5, S5);
    applySi(elements, result, 6, S6);
    applySi(elements, result, 7, S7);
    applySi(elements, result, 8, S8);
    return result;
}

//Aplica la funcion f del algoritmo 
unsigned char* fun(unsigned char *elements, unsigned char* key) {
    unsigned char *e = applyPermutation(elements, E, 48);
    unsigned char *xor = applyFunction(xorFunction, e, key, 48);
    unsigned char *s = applyS(xor);
    unsigned char *result = applyPermutation(s, PRIMITIVE_P, 32);
    free(e);
    free(xor);
    free(s);
    return result;
}

// Reliza la chamba de cifrado y decifrado segun el booleano decrypt
unsigned char * encryption_aux(unsigned char *elements, unsigned char* key, int decrypt) { //return cipher text
    unsigned char *ip = applyPermutation(elements, IP, 64);
    unsigned char *l = substr(ip, 0, 32);
    unsigned char *r = substr(ip, 32, 64);
    unsigned char **keys = splitKeys(key);
    free(ip);
    int i, j;
    for(i = 0, j = 15; i < 16; i++, j--) {
        unsigned char *f;
        if(decrypt)
            f = fun(r, keys[j]);
        else
            f = fun(r, keys[i]);
        unsigned char *ls = r;
        unsigned char *rs = applyFunction(xorFunction, l, f, 32);
        free(f);
        free(l);
        l = ls;
        r = rs;
    }
    unsigned char *rl =catstr(r, l, 32, 32);
    unsigned char *result = applyPermutation(rl, IP_INV, 64);
    free(rl);
    free(l);
    free(r);
    free_keys(keys);
    return result;
}

//DES encryption (1 block)
unsigned char * encryption(unsigned char *elements, unsigned char* key) {
    return encryption_aux(elements, key, 0);
}

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

    size_t n,t;
    //getting the file size the dumb way
    while ((ch = fgetc(fp)) != EOF)
      t++;
    fp = fopen((char *)file_name,"r"); //recovery of fp
    printf("Tamaño de archivo (?): %i\n",t);
    size_t norm = t+8-(t%8);
    unsigned char *out = malloc(norm);
    for(size_t i = 0; i < t; i++){
      ch = fgetc(fp);
      out[i] = (char) ch;
    }
    out[norm] = '\0';
    printf("Antes: %i\nNormalizado en bytes: %i\nRead: %s\n",t,norm,out);
    //complete
    
    fclose(fp);
    fclose(fw);
    return out;
}

//DES decrypt (1 block)

unsigned char * decrypt(unsigned char* elements , unsigned char *key) {
    return encryption_aux(elements, key, 1);
}


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
    unsigned char k[8] = {19,52,87,121,155,188,223,241};
    imprime(k, 64);
    unsigned char** ks =splitKeys(k);
    int i;
    imprime_ks(ks, DES_ITERATIONS, 48);
    free_keys(ks);
    */ 
    // Prueba getChar
    /*
    unsigned char k[4] = {19,52,87,121};
    imprime(k, 4*8);
    imprime_n(getChar(k, 25, 32));
    */

    // Prueba getRow getCol
    /*
    unsigned char c = -1 << 1;
    int m = 1 << 7;
    c = 20;
    imprime_n(c);
    imprime_n(getRow(c));
    imprime_n(getCol(c));
    */

    //Prueba applySi
    /*
    unsigned char k[6] = {19,52,87,121, 13, 1};
    imprime(k, 6*8);
    unsigned char *result = (unsigned char *) calloc(4,sizeof(unsigned char));
    imprime(result, 4*8);
    applySi(k, result, 1, S1);
    applySi(k, result, 2, S2);
    applySi(k, result, 3, S3);
    imprime(result, 4*8);
    free(result);
    */

    //Prueba fun
    /*
    unsigned char key[6] = {27, 2, 239, 252, 112, 114};
    imprime(key, 6*8);
    unsigned char r[4] = {240, 170, 240, 170};
    imprime(r, 4*8);
    unsigned char *f = fun(r, key);
    imprime(f, 32);
    free(f);
    */

    //Prueba encryption decrypt
    /*
    unsigned char k[8] = {19,52,87,121,155,188,223,241};
    unsigned char m[8] = {1,35,69,103,137,171,205,239};
    unsigned char *e = encryption(m,k);
    imprime(e, 64);
    unsigned char *d = decrypt(e, k);
    imprime(d, 64);
    free(e);
    free(d);
    */

    /*
    unsigned char k[8] = {14,50,146,50,234,109,13,115};
    unsigned char m[8] = {135, 135, 135, 135, 135, 135, 135, 135};
    imprime(m, 64);
    unsigned char *e = encryption(m,k);
    imprime(e, 64);
    unsigned char *d = decrypt(e, k);
    imprime(d, 64);
    free(e);
    free(d);
    */
    unsigned char* ef = encryptionFile((unsigned char*)"des.h", (unsigned char*)"hola");
    free(ef);
    return 0;
}
