## Android applicaton for sharing rides between drivers and passengers ##

### Details ###
- Version: Android Studio Ladybug 2024.2.1
- API 24
- Device: Nexus 4
- Database: SQLite
- ***Change the value of MAPS_API_KEY in the file local.properties to access Google Maps.***

### Functionalities ###

- Users register with username and password. When logining, the user chooses whether they will be a driver or a passenger. Same user can have different roled in different login sessions.
- The user's name and rating is displayed. 
- If they are a passenger, the user is redirected to a map and their current location is taken. On the map, the user chooses their destination.
- If they are a driver, they enter an time interval of service (e.g. 50 minutes, starting in that moment), base price, price per km and vehicle. With clicking "Start", the driver starts an offer and it is active in the given time interval. The driver must have entered a vehicle and a number plate. They can also change it.
- In the passenger's view, the active offers are displayed in a recycler view ordered by their rating. In each row the passenger can see: the driver's name, their vehicle and number plate, the price for the ride and the driver's rating. This view is adapted to Landscape mode, too.
- Calculation of price: the distance between the user's current location and the user's destination is multiplied by the price per km that the driver has defined, and it is added to the base price.
- The passenger accepts the offer by clicking on "Accept". They are then redirected to a page showing the number plate they should look for.
- The route is considered active now.
- At the driver's side, they are shown the route they should drive as well as the passenger's name.
- The user ends the ride by clicking "End of ride". Then they rate the driver with 0-5 stars with a 0.5 step. The driver also rates the passenger. The route ends here.
- The rating of each user is calculated as the ratings they get as a driver and as an user.
- In "Your rides", in every row of the recycler vies, users can see their history of rides.
- By clicking "Details" they are shown a more detailed information about the chosen ride: name of driver, name of passenger, their ratings to each other, price, vehicle, number plate.
  
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




