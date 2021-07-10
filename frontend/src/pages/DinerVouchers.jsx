import React, { useState, useEffect, useContext } from "react";
import NavBar from "../components/Navbar";
import { MainContent } from "../styles/MainContent";
import { Box, Checkbox, FormControlLabel } from "@material-ui/core";
import { Redirect } from "react-router";
import DinerVoucher from "../components/DinerVoucher";

import { StoreContext } from "../utils/store";

export default function DinerVouchers() {
    const context = useContext(StoreContext);
    const setAlertOptions = context.alert[1];
    const token = context.auth[0];
    const [showHistory, setShowHistory] = useState(false);
    const [vouchers, setVouchers] = useState([]);

    useEffect(() => {
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
            } else {
                console.log("cannot get vouchers");
            }
        };
        getVouchers();
    }, []);

    if (token === null) return <Redirect to="/" />;

    // when show historical checkbox is clicked
    const handleHistory = (event) => {
        setShowHistory(event.target.checked);
    };

    const getCurrentVouchers = () => {
        if (!vouchers) return;
        if (vouchers.length === 0) return <div>no current vouchers</div>;
        return vouchers.map((item, key) => {
            return (
                !item.used &&
                item.isActive && (
                    <DinerVoucher
                        duration={item.Duration}
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
                {/* // TODO - MAKE BEN'S BOX INTO A COMPONENT */}
                <Box
                    display="flex"
                    flexDirection="column"
                    justifyContent="center"
                    alignItems="center"
                >
                    <h1>My Vouchers</h1>
                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={showHistory}
                                onChange={handleHistory}
                            />
                        }
                        label="Show Historical"
                    />
                    <Box
                        mt={2}
                        width="80vw"
                        height="60vh"
                        border="3px solid #4F4846"
                        bgcolor="#E8CEBF"
                        mb={5}
                        overflow="auto"
                    >
                        {getCurrentVouchers()}
                        {showHistory && getPastVouchers()}
                    </Box>
                </Box>
            </MainContent>
        </>
    );
}
