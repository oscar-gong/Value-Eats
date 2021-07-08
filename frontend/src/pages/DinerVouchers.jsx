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

    if (token === null) return <Redirect to="/" />;

    // when show historical checkbox is clicked
    const handleHistory = (event) => {
        setShowHistory(event.target.checked);
    };

    const getCurrentVouchers = () => {
        return <div>current vouchers</div>;
    };

    const getPastVouchers = () => {
        return <div>past vouchers</div>;
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
                        <DinerVoucher />
                    </Box>
                </Box>
            </MainContent>
        </>
    );
}
