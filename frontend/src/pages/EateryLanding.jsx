import React, {useContext, useState, useEffect } from "react";
import NavBar from "../components/Navbar";
import { MainContent } from "../styles/MainContent";
import { StoreContext } from "../utils/store";
import { Redirect } from "react-router-dom";
import { Box } from "@material-ui/core";
import { ButtonStyled } from "../styles/ButtonStyle";
import EateryVoucher from "../components/EateryVoucher";
import EditCreateVoucher from "../components/EditCreateVoucher";

export default function EateryLanding() {
  const context = useContext(StoreContext);
  const auth = context.auth[0];
  const isDiner = context.isDiner[0];
  console.log(auth);
  console.log(isDiner);
  const [eateryDetails, setEateryDetails] = useState({});
  const [openCreateDiscount, setOpenCreateDiscount] = useState(false);
  useEffect(() => {
    const getEateryDetails = async () => {
      const response = await fetch(
          `http://localhost:8080/eatery/profile/details`,
          {
              method: "GET",
              headers: {
                  Accept: "application/json",
                  "Content-Type": "application/json",
                  Authorization: auth,
              },
          }
      );

      const responseData = await response.json();
      if (response.status === 200) {
          console.log(responseData);
          setEateryDetails(responseData);
      } else {
          // TODO
          console.log(responseData);
      }
    };
    getEateryDetails();
  }, [auth]);

  if (auth === null) return <Redirect to="/" />;
  if (isDiner === "true") return <Redirect to="/DinerLanding" />;
  console.log(isDiner);


  return (
    <>
      <NavBar isDiner={isDiner}/>
      <MainContent>
        <Box display="flex" flexDirection="column" justifyContent="center" alignItems="center">
          <h1>{eateryDetails.name}'s Discounts</h1>
          <Box mt={2}
            width="80vw"
            height="60vh"
            border="3px solid #4F4846"
            bgcolor="#E8CEBF"
            mb={5}
            overflow="auto"
            >
            <EateryVoucher discount={50} isDineIn={true} vouchersLeft={5} timeRemaining={"00:30:59"} ></EateryVoucher>
            <EateryVoucher discount={50} isDineIn={true} vouchersLeft={5} timeRemaining={"00:30:59"} ></EateryVoucher>
            <EateryVoucher discount={50} isDineIn={true} vouchersLeft={5} timeRemaining={"00:30:59"} ></EateryVoucher>
            <EateryVoucher discount={50} isDineIn={true} vouchersLeft={5} timeRemaining={"00:30:59"} ></EateryVoucher>
            <EateryVoucher discount={50} isDineIn={true} vouchersLeft={5} timeRemaining={"00:30:59"} ></EateryVoucher>
          </Box>
        </Box>
        <Box mb={5}
          display="flex"
          justifyContent="space-evenly"
        >
          <ButtonStyled variant="contained"
            color="primary"
          >Verify Voucher</ButtonStyled>
          <ButtonStyled variant="contained"
            color="primary"
            onClick={() => setOpenCreateDiscount(true)}
          >Create Discount</ButtonStyled>
        </Box>
      </MainContent>
      <EditCreateVoucher voucherId={-1}
        open={openCreateDiscount}
        setOpen={setOpenCreateDiscount}
        isEdit={false}
      ></EditCreateVoucher>
    </>
  );
}
