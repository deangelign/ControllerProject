clc
clear

syms p1 p2 p3 x1 x2 u z;
%F = 1/(s - 2)^2;




A = [-0.0656448135812 0; 0.0656448135812 -0.0656448135812];
B = [0.296432; 0];
C = [0 1];

G = [0.99345701778 0;0.00652153007329 0.99345701778];
H = [0.0295429 ;0.0000968601]; 

G_aumentado = [G H;0 0 0];
H_aumentado = [0 0 1]';



qc = (G_aumentado * G_aumentado * G_aumentado) -  ( (p1 + p2 + p3) * (G_aumentado * G_aumentado) ) + (p1*p2 + p3*p1 + p3*p2)*(G_aumentado) - eye(3,3)*(p1*p2*p3);

Wc_aumentado = [H_aumentado G_aumentado*H_aumentado G_aumentado*G_aumentado*H_aumentado];

K_chapeu = [0 0 1]*inv(Wc_aumentado)*qc;

matrizDoida = [(G-eye(2,2)) H;(C*G) (C*H)];

kzinho = ( K_chapeu + [0 0 1] ) *inv(matrizDoida);


k21 = kzinho(1);
k22 = kzinho(2);
k1 = kzinho(3);

k2 = [k21 k22];

matrizDoida2 = [G H;(k2 - k2*G - k1*C*G) (1 - k2*H - k1*C*H)];
XU = matrizDoida2*[x1 ;x2; u];

eq = XU(3);

%iden = eye(2,2)*s

%AA = iden - A

%AA_inv = inv(AA);

%G = ilaplace(AA_inv) 

%G = ilaplace(AA)

%ilaplace(F, x);


