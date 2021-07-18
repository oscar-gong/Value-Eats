import React, {useContext, useState, useEffect } from "react";
import NavBar from "../components/Navbar";
import { MainContent } from "../styles/MainContent";
import { StoreContext } from "../utils/store";
import { Redirect, useHistory } from "react-router-dom";
import { Box } from "@material-ui/core";
import { ButtonStyled } from "../styles/ButtonStyle";
import EateryVoucher from "../components/EateryVoucher";
import EditCreateVoucher from "../components/EditCreateVoucher";
import { logUserOut } from "../utils/logoutHelper";
import { VoucherContainer } from "../styles/VoucherContainer";
import { Subtitle } from "../styles/Subtitle";

export default function EateryLanding() {
  const context = useContext(StoreContext);
  const auth = context.auth[0];
  const isDiner = context.isDiner[0];
  const history = useHistory();
  console.log(auth);
  console.log(isDiner);
  const [eateryDetails, setEateryDetails] = useState({});
  const [openCreateDiscount, setOpenCreateDiscount] = useState(false);

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
    } else if (response.status === 401) {
      logUserOut();
    }else {
      // TODO
      console.log(responseData);
    }
  };

  useEffect(() => {
    getEateryDetails();
  }, [auth]);

  if (auth === null) return <Redirect to="/" />;
  if (isDiner === "true") return <Redirect to="/DinerLanding" />;
  console.log(isDiner);


  return (
    <>
      <NavBar isDiner={isDiner}/>
      <MainContent>
        <Box display="flex" flexDirection="column" alignItems="center">
          <Subtitle>{eateryDetails.name}'s Discounts</Subtitle>
          <VoucherContainer>
            {
              eateryDetails.vouchers && (eateryDetails.vouchers.map((v,key) => {
                return (
                  <EateryVoucher voucherId={v.id}
                    key={key}
                    eateryId={v.eateryId}
                    discount={v.discount}
                    isOneOff={!v.isRecurring}
                    isDineIn={v.eatingStyle === "DineIn" ? true : false}
                    vouchersLeft={v.quantity}
                    date={v.date} 
                    startTime={v.startTime}
                    endTime={v.endTime}
                    timeRemaining={v.duration}
                    nextUpdate={v.nextUpdate ? v.nextUpdate : null}
                    isActive={v.isActive}
                    isRedeemable={v.isRedeemable}
                    refreshList={() => getEateryDetails()}></EateryVoucher>
                );
              }))
            }
            {
              eateryDetails.vouchers && eateryDetails.vouchers.length === 0 &&
              <Box display="flex"
                flexDirection="column"
                minHeight="100%"
                justifyContent="center"
                alignItems="center">
                <h1 style={{marginTop: "0px"}}>
                  No active discounts!
                </h1>
                <ButtonStyled widthPercentage={25}
                  onClick={() => setOpenCreateDiscount(true)}
                >Get started!</ButtonStyled>
              </Box>

            }
          </VoucherContainer>
        </Box>
        <Box display="flex" flexDirection="column" height="30vh">
          <Box
            display="flex"
            justifyContent="space-evenly"
          >
            <ButtonStyled variant="contained"
              color="primary"
              onClick={() => history.push("/RedeemVoucher")}
            >
              Verify Voucher
            </ButtonStyled>
            <ButtonStyled variant="contained"
              color="primary"
              onClick={() => setOpenCreateDiscount(true)}
            >Create Discount</ButtonStyled>
          </Box>
        </Box>
      </MainContent>
      <EditCreateVoucher eateryId={eateryDetails.id} voucherId={-1}
        open={openCreateDiscount}
        setOpen={setOpenCreateDiscount}
        isEdit={false}
        refreshList={() => getEateryDetails()}
      ></EditCreateVoucher>
    </>
  );
}
