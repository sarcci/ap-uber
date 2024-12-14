## Апликација за споделување на превоз помеѓу патник и возач ##

### Детали ###
- Верзија: Android Studio Ladybug 2024.2.1
- API 24
- Уред: Nexus 4
- База: SQLite
- ***Во local.properties да се смени вредноста на MAPS_API_KEY, за пристап до Google Maps***

### Функционалности ###

Корисникот се регистрира во системот со име, корисничко име и лозинка. При најавувањето, одбира дали во 
тековната сесија ќе нуди или бара превоз. Отворена е можноста истиот корисник во една сесија да биде патник, 
а во друга возач. Прикажано е името на патникот и неговиот рејтинг. Доколку е патник, го носи кон мапа и се зема
неговата моментална локација. На мапата, патникот ја избира неговата крајна дестинација. Доколку е возач, внесува 
временски интервал во којшто е слободен (пр. 50 минути, почнувајќи од тој момент), почетна цена на возењето и цена за
километар. Возачот има слобода да ја менува цената во различни понуди во зависност од побарувачката. Во еден момент 
возачот може да има само една активна понуда. Со притискање на копчето „Почни“, ја започнува понудата и таа се смета за
активна во посочениот временски интервал. Претходно возачот треба да има внесено модел на автомобилот којшто го користи
и регистарски таблички. Возачот може да го промени возилото. Кај патникот во RecyclerView се појавуваат возачите кои се
активни, подредени по рејтингот на возачот. Како информации во секој ред се дадени: името на возачот, неговиот модел на
автомобил, пресметаната цена врз база на дестинацијата, и рејтингот на возачот. Екранот со приказ на понудите кај патникот
е прилагоден и за Landscape режим. Патникот ја прифаќа понудата на возачот со кликање на копчето „Прифати“. Откако е притиснато
копчето, рутата се смета за активна (односно возачот не може да вози друга рута). Кај патникот, после прифаќање на понуда, се
појавува View којшто му навестува на моделот на возило и регистарските таблички на возилото кое го очекува. Со кликање на 
„Крај на возењето“ патникот треба да го оцени возачот. При најавување на возачот, доколку патникот го има прифатено возачот, 
кај возачот се појавува мапа, како и рутата којашто треба да ја вози.  Доколку завршило патувањето, при најавување на возачот, 
истиот треба да го оцени и патникот. Со тоа завршува рутата. Рејтингот на секој корисник е пресметан од оцените кои ги добива и 
како возач и како патник. Корисниците можат да ја видат историјата на своите патувања. Во RecyclerView се дадени, во секој ред, 
датумот на патувањето и линк кон повеќе детали за истото. Деталите за секое патување вклучуваат: возач, патник, оцена од возач за
патникот, оцена од патникот, возило, регистрациски таблички, цената и времетраењето на патувањето, како и рутата на истото. 
Дополнителни подобрувања кои може да се додадат на апликацијата се: 
- повеќе патници да можат да се возат на истата рута,
- нотификација кај патникот кога возачот е на дестинацијата.
  
![pic1](https://github.com/user-attachments/assets/82e63266-c645-4769-a4a0-c0a8bd321686)
![pic2](https://github.com/user-attachments/assets/23b25ae2-2c5a-4670-8d08-403df6b5b15e)
![pic3](https://github.com/user-attachments/assets/131b6f67-a19a-4ef8-864d-3dab9bb44b0b)
![picx](https://github.com/user-attachments/assets/2c1b1d00-73a0-4188-a8d2-540f6d0e740f)
![picx2](https://github.com/user-attachments/assets/4e869684-116c-40f8-8763-eb94d1174fa1)
![picx3](https://github.com/user-attachments/assets/d04bfe7e-6986-4d96-8369-287664054024)
![picx8](https://github.com/user-attachments/assets/815f33d1-5227-42db-9d97-61aa1faeb598)
![picx9](https://github.com/user-attachments/assets/efd97fea-7d29-4134-a4fb-4bc9cd7eb959)
![picxx](https://github.com/user-attachments/assets/9fb4e4cc-ae22-4d67-9ed3-44f7df562020)
![picev](https://github.com/user-attachments/assets/665a5be6-c947-4e60-aaf7-c7cf1cba8c12)
![picxx2](https://github.com/user-attachments/assets/af1c559b-be4e-4cdb-823e-94d6f51a524f)
![picx10](https://github.com/user-attachments/assets/034a628a-4b5e-479b-8225-c182ccf77dae)
![pic3xx](https://github.com/user-attachments/assets/e60a05c1-7b95-4f3c-8e0e-5ad44142cf01)




