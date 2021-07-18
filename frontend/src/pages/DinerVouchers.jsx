import React, { useState, useEffect, useContext } from "react";
import NavBar from "../components/Navbar";
import { MainContent } from "../styles/MainContent";
import { Box, Checkbox, FormControlLabel } from "@material-ui/core";
import { Redirect } from "react-router";
import DinerVoucher from "../components/DinerVoucher";
import { VoucherContainer } from "../styles/VoucherContainer";
import { Subtitle } from "../styles/Subtitle";
import { StoreContext } from "../utils/store";
import { logUserOut } from "../utils/logoutHelper";
import { ButtonStyled } from "../styles/ButtonStyle";
import { useHistory } from "react-router-dom";

export default function DinerVouchers() {
    const context = useContext(StoreContext);
    const [token, setAuth] = context.auth;
    const setIsDiner = context.isDiner[1];
    const [showHistory, setShowHistory] = useState(false);
    const [vouchers, setVouchers] = useState([]);
    const history = useHistory();

    const getVouchers = async () => {
        const response = await fetch(
            "http://localhost:8080/diner/voucher",
            {
                method: "GET",
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json",
                    Authorization: token,
                },
            }
        );
        const responseData = await response.json();
        if (response.status === 200) {
            console.log(responseData.vouchers);
            setVouchers(responseData.vouchers);
        } else if (response.status === 401) {
            logUserOut(setAuth, setIsDiner);
        } else {
            console.log("cannot get vouchers");
        }
    };

    useEffect(() => {
        getVouchers();
    }, [token]);

    if (token === null) return <Redirect to="/" />;

    // when show historical checkbox is clicked
    const handleHistory = (event) => {
        setShowHistory(event.target.checked);
    };

    const getCurrentVouchers = () => {
        if (!vouchers) return;
        if (vouchers.length === 0) return (
            <Box display="flex"
            flexDirection="column"
            marginTop="10%"
            alignItems="center"
            height="70vh"
            pt={2}
            >
              <Subtitle style={{color: "black"}}>No Vouchers Purchased Yet..</Subtitle>
              <ButtonStyled widthPercentage={40}
                onClick={() => history.push("/DinerLanding")}
              >
                Find restaurants
              </ButtonStyled>
            </Box>)

        return vouchers.map((item, key) => {
            return (
                !item.used &&
                item.isActive && (
                    <DinerVoucher
                        duration={item.duration}
                        code={item.code}
                        date={item.date}
                        discount={item.discount}
                        eateryID={item.eateryId}
                        eatingStyle={item.eatingStyle}
                        endTime={item.endTime}
                        isActive={item.isActive}
                        isRedeemable={item.isRedeemable}
                        startTime={item.startTime}
                        eateryName={item.eateryName}
                        used={item.used}
                        key={key}
                        handleRefresh={() => getVouchers()}
                    />
                )
            );
        });
    };

    const getPastVouchers = () => {
        if (!vouchers) return;
        return vouchers.map((item, key) => {
            return (
                (item.used || !item.isActive) && (
                    <DinerVoucher
                        code={item.code}
                        date={item.date}
                        discount={item.discount}
                        eateryID={item.eateryId}
                        eatingStyle={item.eatingStyle}
                        endTime={item.endTime}
                        isActive={item.isActive}
                        isRedeemable={item.isRedeemable}
                        startTime={item.startTime}
                        eateryName={item.eateryName}
                        used={item.used}
                        key={key}
                    />
                )
            );
        });
    };

    return (
        <>
            <NavBar isDiner={true} />
            <MainContent>
                <Box
                    display="flex"
                    flexDirection="column"
                    justifyContent="center"
                    alignItems="center"
                >
                    <Subtitle>My Vouchers</Subtitle>
                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={showHistory}
                                onChange={handleHistory}
                            />
                        }
                        label="Show Historical"
                    />
                    <VoucherContainer>
                        {getCurrentVouchers()}
                        {showHistory && getPastVouchers()}
                    </VoucherContainer>
                </Box>
            </MainContent>
        </>
    );
}
