import React, { useState, useEffect } from "react";
import NavBar from "../components/Navbar";
import { MainContainer } from "../styles/MainContainer";
import {
    Typography,
    Grid,
    Box,
    Card,
    Button,
    Modal,
    makeStyles,
} from "@material-ui/core";
import StarRating from "../components/StarRating";
import Review from "../components/Review";
import { useLocation, Redirect, useHistory } from "react-router-dom";
import { StoreContext } from "../utils/store";
import Carousel from "react-material-ui-carousel";
import EditCreateReview from "../components/EditCreateReview";
import ConfirmModal from "../components/ConfirmModal";
import { logUserOut } from "../utils/logoutHelper";
import { handleTimeNextDay } from "../utils/helpers";

const useStyles = makeStyles({
    photo: {
        color: "black",
        transition: "transform 0.15s ease-in-out",
        "&:hover": { transform: "scale3d(1.02, 1.02, 1)", maxHeight: "none" },
        width: "150px",
        height: "150px",
        boxShadow: "0 4px 8px 0 rgba(0, 0, 0, 0.3)",
        objectFit: "contain",
        backgroundColor: "white",
    },
    photoCarousel: {
        width: "400px",
        height: "400px",
        boxShadow: "0 4px 8px 0 rgba(0, 0, 0, 0.3)",
        objectFit: "contain",
        backgroundColor: "white",
        padding: "100px",
    },
    gridContainer: {
        background: "rgba(255, 255, 255, 0.2)",
        marginTop: "10px",
        borderRadius: "10px",
    },
    subtitle: {
        background: "rgba(255, 255, 255, 0.5)",
        borderRadius: "10px",
        margin: "10px 0px",
        padding: "5px 0px",
    },
});

export default function EateryProfile() {
    const [eateryDetails, setEateryDetails] = useState({});
    const location = useLocation();
    const classes = useStyles();
    const context = React.useContext(StoreContext);
    const [auth] = context.auth;
    const [isDiner] = context.isDiner;
    const eateryId = location.pathname.split("/")[3]
        ? location.pathname.split("/")[3]
        : "";
    const [openCreateReview, setOpenCreateReview] = useState(false);
    const [user, setUser] = useState({});
    const [openConfirmModal, setConfirmModal] = useState(false);

    const [open, setOpen] = useState(false);
    const [startTime, setStartTime] = useState("");
    const [endTime, setEndTime] = useState("");
    const [date, setDate] = useState("");
    const [discount, setDiscount] = useState(0);
    const [voucherID, setVoucherID] = useState(0);
    const [code, setCode] = useState("");
    const [isConfirmed, setIsConfirmed] = useState(false);
    const setAlertOptions = context.alert[1];
    const history = useHistory();

    const handleOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    useEffect(() => {
        // on page init, load the users details
        const getUser = async () => {
            const response = await fetch(
                "http://localhost:8080/diner/profile/details",
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
            if (response.status === 200) {
                console.log(responseData);
                setUser({
                    username: responseData.name,
                    email: responseData.email,
                    profilePic: responseData["profile picture"],
                });
                // setEateryList(responseData.eateryList);
            } else if (response.status === 401) {
                logUserOut();
            }
        };
        if (isDiner !== "false") {
            getUser();
        }
    }, [auth, isDiner]);

    useEffect(() => {
        const getEateryDetails = async () => {
            const response = await fetch(
                `http://localhost:8080/eatery/profile/details?id=${eateryId}`,
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
            if (response.status === 200) {
                console.log(responseData);
                if (responseData.vouchers.length > 0) {
                    responseData.vouchers = responseData.vouchers.filter(v => v.isActive);
                } 
                setEateryDetails(responseData);
            } else if (response.status === 401) {
                logUserOut();
            } else {
                // TODO
                console.log(responseData);
            }
        };
        getEateryDetails();
    }, [auth, eateryId, code]);
    if (auth === null) return <Redirect to="/" />;

    const getReviews = () => {
        if (!eateryDetails.reviews) return;
        if (eateryDetails.reviews.length === 0) {
            return <div>no reviews currently</div>;
        }
        console.log(eateryDetails.reviews);
        return eateryDetails.reviews.map((item, key) => {
            return (
                <Review
                    id={item.reviewId}
                    onEateryProfile={false}
                    profilePic={item.profilePic}
                    eateryName={item.eateryName}
                    review={item.message}
                    rating={item.rating}
                    isOwner={item.isOwner}
                    eateryId={item.eateryId}
                    images={item.reviewPhotos ? item.reviewPhotos : []}
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
                <img
                    src={item}
                    alt="menu photos"
                    key={key}
                    className={classes.photoCarousel}
                />
            );
        });
    };

    const getSingleImage = () => {
        if (!eateryDetails.menuPhotos) return;
        if (eateryDetails.menuPhotos.length === 0) {
            return <div>no images currently</div>;
        }
        return (
            <img
                className={classes.photo}
                src={eateryDetails.menuPhotos[0]}
                alt="eatery menu"
                onClick={handleOpen}
            />
        );
    };

    const getNumberOfImages = () => {
        if (!eateryDetails.menuPhotos) return;
        if (eateryDetails.menuPhotos.length === 0) {
            return <div>0 images</div>;
        }
        return <div>{`${eateryDetails.menuPhotos.length} images`}</div>;
    };

    const handleBooking = async () => {
        if (isConfirmed) {
            setIsConfirmed(false);
            setConfirmModal(false);
        } else {
            const response = await fetch(
                `http://localhost:8080/diner/book?id=${voucherID}`,
                {
                    method: "POST",
                    headers: {
                        Accept: "application/json",
                        "Content-Type": "application/json",
                        Authorization: auth,
                    },
                }
            );
            console.log(response);
            const responseData = await response.json();
            if (response.status === 200) {
                console.log(responseData);
                setCode(responseData.data.code);
                setIsConfirmed(true);
                setAlertOptions({
                    showAlert: true,
                    variant: "success",
                    message: responseData.message,
                });
            } else {
                setAlertOptions({
                    showAlert: true,
                    variant: "error",
                    message: responseData.message,
                });
                setConfirmModal(false);
            }
        }
    };

    const handleVoucher = (startTime, endTime, discount, id, date) => {
        setConfirmModal(true);
        setStartTime(startTime);
        setEndTime(endTime);
        setDiscount(discount);
        setDate(date);
        setVoucherID(id);
    };

    const handleCloseModal = () => {
        setConfirmModal(false);
        setIsConfirmed(false);
    };

    const getVouchers = () => {
        if (!eateryDetails.vouchers) return;
        if (eateryDetails.vouchers.length === 0) {
            return <div> no vouchers </div>;
        }
        return eateryDetails.vouchers.map((item, key) => {
            return (
                <Card
                    style={{
                        paddingTop: "25px",
                        paddingBottom: "25px",
                        borderRadius: "0px",
                    }}
                    key={key}
                >
                    <Grid container justify="space-around" alignItems="center">
                        <Grid item style={{ alignItems: "center" }}>
                            <Button
                                variant="contained"
                                color="primary"
                                style={{ display: "block", width: "15vw" }}
                                disabled={
                                    item.disableButton === true ? true : false
                                }
                                onClick={() =>
                                    handleVoucher(
                                        item.startTime,
                                        item.endTime,
                                        item.discount,
                                        item.id,
                                        item.date
                                    )
                                }
                            >
                                {`${item.discount}% OFF - ${item.eatingStyle}`}
                            </Button>
                        </Grid>
                        <Grid item>
                            <Box style={{ margin: "10px" }}>
                                {`${item.quantity} Vouchers Left`}
                            </Box>
                            <Box style={{ margin: "10px" }}>
                                {`${item.date}`}
                            </Box>
                            <Box style={{ margin: "10px" }}>
                                {`Valid from ${item.startTime} - ${handleTimeNextDay(item.endTime)}`}
                            </Box>
                        </Grid>
                    </Grid>
                </Card>
            );
        });
    };

    const getCuisines = () => {
        if (!eateryDetails.cuisines) return;
        return eateryDetails.cuisines.join(", ");
    };

    return (
        <>
            <NavBar isDiner={isDiner} />
            <MainContainer>
                <Grid container spacing={5} className={classes.gridContainer}>
                    <Grid item xs={6}>
                        <Typography variant="h3">
                            {eateryDetails.name}
                        </Typography>
                        <StarRating rating={parseFloat(eateryDetails.rating)} />{" "}
                        {eateryDetails.rating === ".0"
                            ? 0
                            : eateryDetails.rating}
                        <Typography variant="subtitle2">
                            {eateryDetails.address}
                        </Typography>
                        <Typography variant="subtitle2">
                            {getCuisines()}
                        </Typography>
                        <Typography variant="h5" className={classes.subtitle}>
                            Menu Photos
                        </Typography>
                        <Box flex-wrap="wrap" flexDirection="row">
                            {getSingleImage()}
                        </Box>
                        {getNumberOfImages()}
                        <Typography className={classes.subtitle} variant="h5">
                            Reviews
                        </Typography>
                        <Button
                            style={{ margin: "10px 0px" }}
                            variant="contained"
                            color="primary"
                            onClick={() => setOpenCreateReview(true)}
                            disabled={isDiner === "true" ? false : true}
                            // disabled={true}
                        >
                            Write a Review
                        </Button>
                        <Box>{getReviews()}</Box>
                    </Grid>

                    <Grid item xs={6}>
                        <Typography variant="h3">Discounts</Typography>
                        {getVouchers()}
                        <div>
                            <Modal
                                style={{
                                    display: "flex",
                                    alignItems: "center",
                                    justifyContent: "center",
                                }}
                                open={open}
                                onClose={handleClose}
                            >
                                {
                                    <div
                                        style={{
                                            top: "25%",
                                            margin: "auto",
                                            outline: "none",
                                        }}
                                    >
                                        <Carousel
                                            navButtonsProps={{
                                                style: {
                                                    opacity: "50%",
                                                },
                                            }}
                                            navButtonsAlwaysVisible={true}
                                            autoPlay={false}
                                        >
                                            {getImages()}
                                        </Carousel>
                                    </div>
                                }
                            </Modal>
                        </div>
                    </Grid>
                </Grid>
                <EditCreateReview id={-1}
                    eateryId={parseInt(eateryId)}
                    open={openCreateReview}
                    setOpen={setOpenCreateReview}
                    username={user.username}
                    profilePic={user.profilePic}
                    reviewTextState={["", null]}
                    ratingState={["", null]}
                    reviewImagesState={[[], null]}
                    isEdit={false}
                />
                <ConfirmModal
                    open={openConfirmModal}
                    handleClose={handleCloseModal}
                    // eateryId={eateryId}
                    title={!isConfirmed ? "Confirmation" : "Discount Booked!"}
                    message={
                        !isConfirmed
                            ? `Purchase for ${eateryDetails.name} valid for use between ${startTime} - ${handleTimeNextDay(endTime)} on ${date}`
                            : `${discount}% off at ${eateryDetails.name}, CODE: ${code}, Valid between ${startTime} - ${handleTimeNextDay(endTime)}`
                    }
                    denyText={isConfirmed ? "View Vouchers" : "Cancel"}
                    handleDeny={isConfirmed ? () => history.push("/DinerVouchers") : null}
                    handleConfirm={!isConfirmed ? () => handleBooking() : () => handleCloseModal()}
                    confirmText={isConfirmed ? "Ok" : "Confirm"}
                ></ConfirmModal>
            </MainContainer>
        </>
    );
}
