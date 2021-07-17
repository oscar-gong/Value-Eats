import React, { useState } from "react";
import { Box, Button } from "@material-ui/core";
import { useHistory } from "react-router-dom";
import Countdown from "react-countdown";
import { handleTimeNextDay } from "../utils/helpers";
export default function DinerVoucher({
    duration,
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
    used,
}) {
    const history = useHistory();

    const handleViewEateryClick = () => {
        history.push(`/EateryProfile/${eateryName}/${eateryID}`);
    };
    // not redeemable  means it is complete
    // redeemable means it has a countdown
    const [complete, setComplete] = useState(!isRedeemable);
    const handleComplete = () => {
        setComplete(true);
    };

    return (
        <Box
            display="flex"
            justifyContent="space-around"
            alignItems="center"
            border="3px solid #4F4846"
            bgcolor={used || !isActive ? "#808080" : "#E8CEBF"}
            margin="20px"
        >
            <Box display="flex" flexDirection="column">
                <h1>
                    {isActive
                        ? used
                            ? "USED"
                            : "ACTIVE"
                        : used
                        ? "USED"
                        : "EXPIRED"}
                </h1>
            </Box>
            <Box display="flex" flexDirection="column">
                <h1>
                    {discount}% off - {eatingStyle}
                </h1>
            </Box>
            <Box display="flex" flexDirection="column">
                {((!isRedeemable && !used) || (!used && complete)) && (
                    <h3 style={{ margin: "5px 0px" }}>
                        Use on {date} between {startTime} - {handleTimeNextDay(endTime)}
                    </h3>
                )}
                {isRedeemable && !used && !complete && (
                    <h3 style={{ margin: "5px 0px" }}>
                        Time remaining{" "}
                        <Countdown
                            onComplete={handleComplete}
                            date={Date.now() + duration}
                        />
                    </h3>
                )}

                {!used && <h3 style={{ margin: "5px 0px" }}>Code: {code}</h3>}
                {used && (
                    <h3 style={{ margin: "5px 0px" }}>Redeemed on {date}</h3>
                )}
            </Box>
            <Box display="flex" flexDirection="column" justifyContent="center">
                <Box display="flex" justifyContent="center">
                    <Button onClick={handleViewEateryClick} variant="contained">
                        view restaurant
                    </Button>
                </Box>
            </Box>
        </Box>
    );
}
