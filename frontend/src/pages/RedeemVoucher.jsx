import React, { useContext, useState, useEffect } from "react";
import NavBar from "../components/Navbar";
import { MainContent } from "../styles/MainContent";
import { StoreContext } from "../utils/store";
import { Redirect, useHistory } from "react-router-dom";
import { Box } from "@material-ui/core";
import { ButtonStyled } from "../styles/ButtonStyle";
import { PageTitle } from "../styles/PageTitle";
import { logUserOut } from "../utils/logoutHelper";
import ConfirmModal from "../components/ConfirmModal";
import Confetti from "react-confetti";
import { ShakeHead } from "../styles/ShakeHead";
import request from "../utils/request";
import { MainContainer } from "../styles/MainContainer";

export default function RedeemVoucher () {
  const context = useContext(StoreContext);
  const [auth, setAuth] = context.auth;
  const [isDiner, setIsDiner] = context.isDiner;
  const setAlertOptions = context.alert[1];
  const history = useHistory();
  const [code, setCode] = useState({ value: "", valid: true });
  const [openRedeemed, setOpenRedeemed] = useState(false);
  console.log(auth);
  console.log(isDiner);

  // Window resize boiler plate code adapted from: https://www.codegrepper.com/code-examples/javascript/react+js+get+window+height
  const getWindowDimensions = () => {
    return {
      width: window.innerWidth,
      height: window.innerHeight,
    };
  };

  const { width, height } = useWindowDimensions();
  function useWindowDimensions () {
    const [windowDim, setWindowDim] = useState(getWindowDimensions());
    useEffect(() => {
      function handleResize () {
        setWindowDim(getWindowDimensions());
      }

      window.addEventListener("resize", handleResize);
      return () => window.removeEventListener("resize", handleResize);
    }, []);

    return windowDim;
  }

  const handleRedeem = async () => {
    console.log("This will call the redeem voucher endpoint");
    const response = await request.post(
      `eatery/verify/voucher?code=${code.value}`,
      {},
      auth
    );
    const responseData = await response.json();
    if (response.status === 200) {
      setAlertOptions({
        showAlert: true,
        variant: "success",
        message: responseData.message,
      });
      setOpenRedeemed(true);
    } else if (response.status === 401) {
      logUserOut(setAuth, setIsDiner);
    } else {
      setCode({ ...code, valid: false });
      setAlertOptions({
        showAlert: true,
        variant: "error",
        message: responseData.message,
      });
    }
  };

  if (auth === null) return <Redirect to="/" />;
  if (isDiner === "true") return <Redirect to="/DinerLanding" />;
  console.log(isDiner);

  return (
    <>
      <Confetti opacity={openRedeemed ? 1 : 0} width={width} height={height} />
      <NavBar isDiner={isDiner} />
      <MainContainer>
        <MainContent>
          <Box
            display="flex"
            flexDirection="column"
            justifyContent="space-evenly"
            alignItems="center"
            height="90vh"
          >
            <PageTitle>Redeem Voucher</PageTitle>
            <Box
              pt={2}
              width="50vw"
              display="flex"
              flexDirection="column"
              justifyContent="center"
            >
              <ShakeHead
                aria-label="outlined-basic"
                label="Enter code here to redeem your voucher"
                animate={!code.valid ? "shake-head" : ""}
                onChange={(e) =>
                  setCode({
                    value: e.target.value,
                    valid: true,
                  })
                }
                error={!code.valid}
                helperText={code.valid ? "" : "Please try again"}
                value={code.value}
                variant="outlined"
                fullWidth
              />
              <Box py={2} display="flex" justifyContent="center">
                <ButtonStyled
                  variant="contained"
                  color="primary"
                  onClick={handleRedeem}
                >
                  Redeem
                </ButtonStyled>
              </Box>
            </Box>
          </Box>
          <Box height="40vh"></Box>
        </MainContent>
      </MainContainer>
      <ConfirmModal
        open={openRedeemed}
        handleClose={() => setOpenRedeemed(false)}
        title={"Voucher Redeemed!!!"}
        message={"Would you like to redeem another?"}
        confirmText={"Redeem again"}
        handleConfirm={() => setOpenRedeemed(false)}
        denyText={"Back to Vouchers"}
        handleDeny={() => history.push("/EateryLanding")}
      ></ConfirmModal>
    </>
  );
}
