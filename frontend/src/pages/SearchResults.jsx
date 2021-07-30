import React, { useState, useContext, useEffect } from "react";
import { StoreContext } from "../utils/store";
import NavBar from "../components/Navbar";
import Loading from "../components/Loading";
import { useLocation } from "react-router-dom";
import { logUserOut } from "../utils/logoutHelper";
import EateryDisplay from "../components/EateryDisplay";
import { MainContainer } from "../styles/MainContainer";
import { Box } from "@material-ui/core";

export default function SearchResults () {
  const context = useContext(StoreContext);
  const [isDiner, setIsDiner] = context.isDiner;
  const [auth, setAuth] = context.auth;
  const [loading, setLoading] = useState(false);
  const [eateryList, setEateryList] = useState([]);
  const location = useLocation();
  const search = location.state ? location.state.search : "";

  useEffect(() => {
    const getResultEateries = async () => {
      setLoading(true);
      console.log(`this is being searched ${search}`);
      const response = await fetch(`http://localhost:8080/recommendation/eatery/fuzzy_search/${search}`,
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
        setEateryList(responseData.eateryList);
      } else if (response.status === 401) {
        logUserOut(setAuth, setIsDiner);
      } else {
        console.log("cannot get search results");
      }
      setLoading(false);
    };
    getResultEateries();
  }, [auth, search, setAuth, setIsDiner]);

  const displayEateryList = () => {
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
          image={item.profilePic}
        />
      );
    });
  };

  return (
    <>
      <NavBar isDiner={isDiner} />
      <MainContainer>
        <Box py={4}>
            <h4>{`Found ${eateryList.length} Result${
                eateryList.length === 1 ? "" : "s"
            } for "${search}"`}</h4>
            {
              !loading &&
              displayEateryList()
            }
        </Box>
        <Loading isLoading={loading} />
      </MainContainer>
    </>
  );
}
