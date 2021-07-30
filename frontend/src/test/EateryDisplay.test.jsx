import { shallow } from "enzyme";
import React from "react";
import renderer from "react-test-renderer";
import EateryDisplay from "../components/EateryDisplay";

describe("EateryDisplay", () => {
  it("can work in the base case", () => {
    const display = shallow(<EateryDisplay name={"Test Eatery"}
      id={0}
      discount={"50%"}
      cuisines={["Chinese", "Japanese"]}
      rating={2.5}/>);
    expect(display.exists()).toBeTruthy();
  });

  it("displays the discount correctly", () => {
    const display = shallow(<EateryDisplay name={"Test Eatery"}
      id={0}
      discount={"50%"}
      cuisines={["Chinese", "Japanese"]}
      rating={2.5}/>);
    console.log(display.children().at(0).prop("title"));

    expect(display.children().at(0).prop("title") === "UP TO 50% OFF");
  });

  it("displays the eateries correctly", () => {
    const display = shallow(<EateryDisplay name={"Test Eatery"}
      id={0}
      discount={"50%"}
      cuisines={["Chinese", "Japanese"]}
      rating={2.5}/>);
    expect(display.text().includes("Chinese, Japanese")).toBe(true);
  });

  it("displays the eatery name correctly", () => {
    const display = shallow(<EateryDisplay name={"Test Eatery"}
      id={0}
      discount={"50%"}
      cuisines={["Chinese", "Japanese"]}
      rating={2.5}/>);
    expect(display.text().includes("Test Eatery")).toBe(true);
  });

  /* Snapshot tests - creates snapshots for new tests, otherwise will check the existing tests. To update existing tests,
  follow the prompts on the screen after running test. Done to avoid regression. */

  it("renders basic component", () => {
    const display = renderer.create(<EateryDisplay name={"Test Eatery"}
      id={0}
      discount={"50%"}
      cuisines={["Chinese", "Japanese"]}
      rating={2.5}
    />);
    expect(display).toMatchSnapshot();
  });
});
