import React, { useContext, useState, useEffect } from "react";
import { Box, Button } from "@material-ui/core";
import { useHistory } from "react-router-dom";
export default function DinerVoucher({
    code,
    date,
    discount,
    eateryID,
    eatingStyle,
    endTime,
    isActive,
    isRedeemable,
    startTime,
    eateryName,
}) {
    const history = useHistory();
    const handleViewEateryClick = () => {
        history.push(`/EateryProfile/${eateryName}/${eateryID}`);
    };

    return (
        <Box
            display="flex"
            justifyContent="space-around"
            alignItems="center"
            border="3px solid #4F4846"
            bgcolor={isRedeemable ? "#E8CEBF" : "#808080"}
            margin="20px"
        >
            <Box display="flex" flexDirection="column">
                <h1>{isRedeemable ? "ACTIVE" : "USED"}</h1>
            </Box>
            <Box display="flex" flexDirection="column">
                <h1>
                    {discount}% off - {eatingStyle}
                </h1>
            </Box>
            <Box display="flex" flexDirection="column">
                <h3 style={{ margin: "5px 0px" }}>Time remaining {endTime}</h3>
                <h3 style={{ margin: "5px 0px" }}>Code: {code}</h3>
            </Box>
            <Box display="flex" flexDirection="column" justifyContent="center">
                <Box display="flex" justifyContent="center">
                    <Button onClick={handleViewEateryClick}>
                        view restaurant
                    </Button>
                </Box>
            </Box>
        </Box>
    );
}
