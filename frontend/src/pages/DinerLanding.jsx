import React, { useState, useEffect } from "react";
import NavBar from "../components/Navbar";
import StarRating from "../components/StarRating";
import { MainContainer } from "../styles/MainContainer";
import Carousel from "react-material-ui-carousel";
import { useHistory } from "react-router";
import {
    Card,
    Grid,
    CardContent,
    CardHeader,
    CardMedia,
    Typography,
    makeStyles,
    Select,
    MenuItem,
    InputLabel,
    Box,
} from "@material-ui/core";

const useStyles = makeStyles({
    card: {
        color: "black",
        borderRadius: "0px",
        transition: "transform 0.15s ease-in-out",
        "&:hover": { transform: "scale3d(1.02, 1.02, 1)", maxHeight: "none" },
        maxHeight: "300px",
    },
    wideCard: {
        marginTop: "20px",
        transition: "transform 0.15s ease-in-out",
        "&:hover": { transform: "scale3d(1.02, 1.02, 1)" },
    },
});

export default function DinerLanding({ token }) {
    const [eateryList, setEateryList] = useState([]);
    const [hover, setHover] = useState(false);
    const classes = useStyles();
    const history = useHistory();

    useEffect(() => {
        const getEateryList = async () => {
            const response = await fetch(
                "http://localhost:8080/list/eateries",
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
                console.log(responseData.eateryList);
                setEateryList(responseData.eateryList);
            }
        };
        getEateryList();
    }, [token]);

    const getCuisineList = (cuisines) => {
        let cuisineString = cuisines.join(", ");
        if (cuisineString.length > 25) {
            cuisineString = cuisineString.substring(0, 24) + "...";
        }
        return cuisineString;
    };

    const getSlides = () => {
        if (!eateryList) return;
        const splitEateryList = [];
        for (var i = 0; i < eateryList.length; i++) {
            let subList = eateryList.slice(i, i + 3);
            splitEateryList.push(subList);
            i = i + 2;
        }
        console.log(splitEateryList);

        return splitEateryList.map((item, key) => {
            return (
                <Grid
                    container
                    justify="space-between"
                    alignItems="center"
                    direction="row"
                    key={key}
                >
                    {Array.from({ length: 3 }, (x, i) => {
                        return (
                            <Grid item xs={4} key={i}>
                                {item[i] && (
                                    <Card
                                        className={classes.card}
                                        onClick={(e) =>
                                            history.push(
                                                `/EateryProfile/${item[i].name}/${item[i].id}`
                                            )
                                        }
                                        onMouseLeave={() => setHover(true)}
                                        onMouseEnter={() => setHover(false)}
                                    >
                                        <CardHeader
                                            title={
                                                "UP TO " +
                                                item[i].discount +
                                                " OFF"
                                            }
                                        />
                                        <CardMedia
                                            style={{
                                                height: "150px",
                                            }}
                                            image={
                                                "https://i.pinimg.com/originals/b8/e1/4a/b8e14a14af9434aa5ccc0376a47a5237.jpg"
                                            }
                                        />
                                        <CardContent>
                                            <Grid
                                                container
                                                justify="space-between"
                                                alignItems="flex-end"
                                            >
                                                <Grid item xs={8}>
                                                    <Typography variant="h5">
                                                        {item[i].name}
                                                    </Typography>
                                                    <Typography variant="subtitle2">
                                                        {hover
                                                            ? getCuisineList(
                                                                  item[i]
                                                                      .cuisines
                                                              )
                                                            : item[
                                                                  i
                                                              ].cuisines.join(
                                                                  ", "
                                                              )}
                                                    </Typography>
                                                </Grid>
                                                <Grid item xs={4}>
                                                    <StarRating
                                                        rating={item[i].rating}
                                                    />
                                                </Grid>
                                            </Grid>
                                        </CardContent>
                                    </Card>
                                )}
                            </Grid>
                        );
                    })}
                </Grid>
            );
        });
    };

    const getEateries = () => {
        if (!eateryList) return;
        return eateryList.map((item, key) => {
            return (
                <Card
                    className={classes.wideCard}
                    onClick={(e) =>
                        history.push(`/EateryProfile/${item.name}/${item.id}`)
                    }
                    key={key}
                >
                    <CardHeader title={"UP TO " + item.discount + " OFF"} />
                    <CardMedia
                        style={{
                            height: "150px",
                        }}
                        image={
                            "https://i.pinimg.com/originals/b8/e1/4a/b8e14a14af9434aa5ccc0376a47a5237.jpg"
                        }
                    />
                    <CardContent>
                        <Grid
                            container
                            justify="space-between"
                            alignItems="flex-end"
                        >
                            <Grid item xs={8}>
                                <Typography variant="h5">
                                    {item.name}
                                </Typography>
                                <Typography variant="subtitle2">
                                    {item.cuisines.join(", ")}
                                </Typography>
                            </Grid>
                            <Grid item>
                                <Grid item container>
                                    <Grid item>
                                        <StarRating rating={item.rating} />
                                    </Grid>
                                    <Grid
                                        item
                                        style={{
                                            marginLeft: "10px",
                                            marginTop: "4px",
                                        }}
                                    >
                                        {item.rating}
                                    </Grid>
                                </Grid>
                            </Grid>
                        </Grid>
                    </CardContent>
                </Card>
            );
        });
    };
    return (
        <>
            <NavBar token={token} isDiner={true} />
            <MainContainer>
                <Typography variant="h5">Hi username,</Typography>
                <Box textAlign="right">
                    <InputLabel id="demo-simple-select-outlined-label">
                        Sort by
                    </InputLabel>
                    <Select
                        labelId="demo-simple-select-outlined-label"
                        id="demo-simple-select-outlined"
                        value={10}
                        onChange={(e) => console.log("changed")}
                    >
                        <MenuItem value={10}>Distance</MenuItem>
                        <MenuItem value={20}>Rating</MenuItem>
                        <MenuItem value={30}>New</MenuItem>
                    </Select>
                </Box>
                <Typography variant="h6">
                    Restaurants we think you would like
                </Typography>

                <Carousel>{getSlides()}</Carousel>
                {getEateries()}
            </MainContainer>
        </>
    );
}
