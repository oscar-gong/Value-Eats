import React, { useState } from "react";
import { Box, Button, Grid } from "@material-ui/core";
import { useHistory } from "react-router-dom";
import Countdown from "react-countdown";
import { handleTimeNextDay } from "../utils/helpers";
import { ButtonStyled } from "../styles/ButtonStyle";
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
    handleRefresh,
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
            border="2px solid #FF845B"
            borderRadius="20px"
            bgcolor={used || !isActive ? "#d6d6d6" : "white"}
            margin="20px"
        >
            <Grid
                container
                direction="row"
                justifyContent="center"
                alignItems="center"
            >
                <Grid item display="flex" flexDirection="column" xs={2}>
                    <Box display="flex" flexDirection="column" alignItems="center">
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
                </Grid>
                <Grid item display="flex" justifyContent="center" xs={3}>
                    <Box display="flex" flexDirection="column" alignItems="center" flexWrap="nowrap">
                        <h2>
                            {discount}% off - {eatingStyle}
                        </h2>
                    </Box>
                </Grid>
                <Grid
                    item
                    display="flex"
                    flexDirection="column"
                    justifyContent="center"
                    xs={4}
                >
                    <Box display="flex" flexDirection="column" alignItems="center" justifyContent="center" textAlign="center">
                        {!isRedeemable && !used && (
                            <h3 style={{ margin: "5px 0px" }}>
                                Use on {date} between {startTime} -
                                {handleTimeNextDay(endTime)}
                            </h3>
                        )}
                        {isRedeemable && !used && (
                            <h3 style={{ margin: "5px 0px" }}>
                                Time remaining{" "}
                                <Countdown
                                    onComplete={handleRefresh}
                                    date={Date.now() + duration - 1000}
                                />
                            </h3>
                        )}

                        {!used && (
                            <h3 style={{ margin: "5px 0px" }}>Code: {code}</h3>
                        )}
                        {used && (
                            <h3 style={{ margin: "5px 0px" }}>
                                Redeemed on {date}
                            </h3>
                        )}
                    </Box>
                </Grid>
                <Grid item display="flex" justifyContent="center" xs={3}>
                    <Box
                        display="flex"
                        flexDirection="column"
                        justifyContent="center"
                    >
                        <Box display="flex" justifyContent="center"> 
                            <ButtonStyled
                                onClick={handleViewEateryClick}
                                variant="contained"
                            >
                                view restaurant
                            </ButtonStyled>
                        </Box>
                    </Box>
                </Grid>
            </Grid>
        </Box>
    );
}
