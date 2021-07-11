import React, {useContext, useState, useEffect } from "react";
import NavBar from "../components/Navbar";
import { MainContent } from "../styles/MainContent";
import { StoreContext } from "../utils/store";
import { Redirect } from "react-router-dom";
import { Box, TextField } from "@material-ui/core";
import { ButtonStyled } from "../styles/ButtonStyle";
import { Title } from "../styles/Title";

export default function RedeemVoucher() {
  const context = useContext(StoreContext);
  const auth = context.auth[0];
  const isDiner = context.isDiner[0];
  const [code, setCode] = useState("");
  console.log(auth);
  console.log(isDiner);
//   useEffect(() => {
//     const getEateryDetails = async () => {
//       const response = await fetch(
//           `http://localhost:8080/eatery/profile/details`,
//           {
//               method: "GET",
//               headers: {
//                   Accept: "application/json",
//                   "Content-Type": "application/json",
//                   Authorization: auth,
//               },
//           }
//       );

//       const responseData = await response.json();
//       if (response.status === 200) {
//           console.log(responseData);
//           setEateryDetails(responseData);
//       } else {
//           // TODO
//           console.log(responseData);
//       }
//     };
//     getEateryDetails();
//   }, [auth]);

  const handleRedeem = () => {
    console.log("This will call the redeem voucher endpoint");
  }

  if (auth === null) return <Redirect to="/" />;
  if (isDiner === "true") return <Redirect to="/DinerLanding" />;
  console.log(isDiner);


  return (
    <>
      <NavBar isDiner={isDiner}/>
      <MainContent>
        <Box display="flex" flexDirection="column" justifyContent="space-evenly" alignItems="center" height="90vh">
          <Title>Redeem Voucher</Title>
          <Box pt={2} width="50vw" display="flex" flexDirection="column" justifyContent="center">
              <TextField
                id="outlined-basic"
                label="Enter code here to redeem your voucher"
                onChange={(e) =>
                  setCode(e.target.value)
                }
                value={code}
                variant="filled"
                fullWidth
              />
              <Box py={2} display="flex" justifyContent="center">
                <ButtonStyled variant="contained"
                  color="primary"
                  onClick={handleRedeem}
                >Redeem</ButtonStyled>
              </Box>
            </Box>
        </Box>
        <Box height="40vh">
        </Box>
      </MainContent>
    </>
  );
}
