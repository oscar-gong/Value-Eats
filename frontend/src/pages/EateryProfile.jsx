import React, { useState, useEffect } from "react";
import NavBar from "../components/Navbar";
import { MainContainer } from "../styles/MainContainer";
import { Typography, Grid, Box, Card, Button } from "@material-ui/core";
import StarRating from "../components/StarRating";
import Review from "../components/Review";
import { useLocation, useHistory } from "react-router-dom";

export default function EateryProfile({ token }) {
    const [eateryDetails, setEateryDetails] = useState([]);
    const location = useLocation();

    const eateryId = location.pathname.split("/")[3];

    useEffect(() => {
        const getEateryDetails = async () => {
            const response = await fetch(
                `http://localhost:8080/eatery/${eateryId}/details`,
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
                console.log(responseData);
                setEateryDetails(responseData);
            }
            console.log(token);
        };
        getEateryDetails();
    }, [token, eateryId]);

    const getReviews = () => {
        if (!eateryDetails.reviews) return;
        if (eateryDetails.reviews.length === 0) {
            return <div>no reviews currently</div>;
        }
        console.log(eateryDetails.reviews);
        return eateryDetails.reviews.map((item, key) => {
            return (
                <Review
                    onEateryProfile={false}
                    eateryName={item.eateryName}
                    review={item.message}
                    rating={item.rating}
                    isOwner={true}
                    key={key}
                ></Review>
            );
        });
    };

    const getImages = () => {
        if (!eateryDetails.menuPhotos) return;
        if (eateryDetails.menuPhotos.length === 0) {
            return <div>no images currently</div>;
        }
        return eateryDetails.menuPhotos.map((item, key) => {
            return (
                // TODO make it responsive
                <img src={item} width="200px" key={key} height="auto" />
            );
        });
    };

    const getVouchers = () => {
        if (!eateryDetails.vouchers) return;
        if (eateryDetails.vouchers.length === 0) {
            return <div> no vouchers </div>;
        }
        return eateryDetails.vouchers.map((item, key) => {
            return (
                <Card style={{ height: "80px" }}>
                    <Grid container justify="space-between">
                        <Grid item>
                            <Button variant="contained" color="primary">{`${
                                item.discount * 100
                            }% OFF - ${item.type}`}</Button>
                        </Grid>
                        <Grid item>
                            <Box>5 LEFT PLACEHOLDER</Box>
                            <Box>VALID 1-2PM PLACEHOLDER</Box>
                        </Grid>
                    </Grid>
                </Card>
            );
        });
    };

    return (
        <>
            <NavBar token={token} isDiner={true} />
            <MainContainer>
                <Grid container spacing={5}>
                    <Grid item xs={6}>
                        <Typography variant="h5">
                            {eateryDetails.name}
                        </Typography>
                        <StarRating rating={eateryDetails.rating} />
                        <Typography variant="h5">
                            {eateryDetails.address}
                        </Typography>
                        <Typography variant="h3">Menu Photos</Typography>
                        <Box flex-wrap="wrap" flexDirection="row">
                            {getImages()}
                        </Box>
                        <Typography variant="h3">Reviews</Typography>

                        <Box>{getReviews()}</Box>
                    </Grid>

                    <Grid item xs={6}>
                        <Typography variant="h3">Discounts</Typography>
                        {getVouchers()}
                    </Grid>
                </Grid>
            </MainContainer>
        </>
    );
}
