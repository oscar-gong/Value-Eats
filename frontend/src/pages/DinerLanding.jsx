import React, { useState, useEffect, useContext } from "react";
import NavBar from "../components/Navbar";
import StarRating from "../components/StarRating";
import { MainContainer } from "../styles/MainContainer";
import Carousel from "react-material-ui-carousel";
import { useHistory, Redirect } from "react-router";
import { StoreContext } from "../utils/store";
import { logUserOut } from "../utils/logoutHelper";
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
    FormControl,
} from "@material-ui/core";
import Loading from "../components/Loading";
import EateryDisplay from "../components/EateryDisplay";

const useStyles = makeStyles({
    card: {
        color: "black",
        borderRadius: "0px",
        transition: "transform 0.15s ease-in-out",
        "&:hover": {
            transform: "scale3d(1.02, 1.02, 1)",
            maxHeight: "none",
            cursor: "pointer",
        },
        maxHeight: "300px",
    },
    wideCard: {
        marginTop: "20px",
        transition: "transform 0.15s ease-in-out",
        "&:hover": {
            transform: "scale3d(1.02, 1.02, 1)",
            cursor: "pointer",
        },
    },
});

export default function DinerLanding({ token }) {
    const [eateryList, setEateryList] = useState([]);
    const [hover, setHover] = useState(true);
    const classes = useStyles();
    const history = useHistory();
    const context = useContext(StoreContext);
    const [auth, setAuth] = context.auth;
    const [isDiner, setIsDiner] = context.isDiner;
    const [name, setName] = useState("");
    const [sortBy, setSortBy] = useState("");
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const getEateryList = async () => {
            setLoading(true);
            const response = await fetch(
                "http://localhost:8080/list/eateries",
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
            setLoading(false);
            if (response.status === 200) {
                console.log(responseData);
                setName(responseData.name);
                setEateryList(responseData.eateryList);
            } else if (response.status === 401) {
                logUserOut(setAuth, setIsDiner);
            }
        };
        getEateryList();
    }, [auth, setAuth, setIsDiner]);

    if (auth === null) return <Redirect to="/" />;
    if (isDiner === "false") return <Redirect to="/EateryLanding" />;

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
                                            history.push({
                                                pathname: `/EateryProfile/${item[i].name}/${item[i].id}`,
                                            })
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
                                                        rating={parseFloat(
                                                            item[i].rating
                                                        )}
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
                <EateryDisplay
                    name={item.name}
                    id={item.id}
                    key={key}
                    discount={item.discount}
                    cuisines={item.cuisines}
                    rating={item.rating}
                />
            );
        });
    };
    return (
        <>
            <NavBar isDiner={isDiner} />
            <MainContainer>
                <Box py={4}>
                    <Typography variant="h5">Hi {name},</Typography>
                    <Box textAlign="right">
                        <FormControl
                            variant="filled"
                            style={{ minWidth: "100px" }}
                        >
                            <InputLabel>Sort By</InputLabel>
                            <Select
                                value={sortBy}
                                onChange={(e) => setSortBy(e.target.value)}
                            >
                                <MenuItem value={"distance"}>Distance</MenuItem>
                                <MenuItem value={"rating"}>Rating</MenuItem>
                                <MenuItem value={"new"}>New</MenuItem>
                            </Select>
                        </FormControl>
                    </Box>

                    <Typography variant="h6">
                        Restaurants we think you would like
                    </Typography>

                    <Carousel>{getSlides()}</Carousel>
                    {getEateries()}
                </Box>
                <Loading isLoading={loading} />
            </MainContainer>
        </>
    );
}
