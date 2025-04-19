#!/bin/bash

clear
echo -e "\033[0;32m"
echo "______________________"
echo "  FLASHCARD REGISTER  "
echo "______________________"
echo "                 @Mytx"

echo ""
echo " - op[T]ions -"

# Configuration
dir="/home/mytx/onyx_saves/flashcards";
cards=("java" "python" "C++" "crafting_interpreters")
keys=("j" "p" "cpp" "craft")

# Option Menu
echo ""
echo "s:show | q:quit | r ?:remove a line | help: Quick Guide"
echo "sort:sorted view | search ?:search words | clr:clear screen"
echo ""
echo ""

size=${#cards[@]}
for ((i=0; i<size; i++))
do
  echo -n ${keys[i]}':['${cards[i]}"] "
done
echo ""
echo ""

echo -n "OwO ~ "  # I couldn't think of anything more cringier than this
read option

# Select Card File
for ((i=0; i<size; i++))
do
  if [ "$option" == "${keys[i]}" ]; then
    selected_card="${cards[i]}";
    echo "Selected Card: [ "$selected_card" ]"
    break
  fi
  if [ $i -eq $((size - 1)) ]; then
    echo "Invalid Card!"
    exit
  fi
done


# Process Flashcard
while [ true ]; do
  echo -n "OwO ~ "  # I couldn't think of anything more cringier than this
  read prompt arg

  if [ "$prompt" == "q" ]; then
    exit
  elif [ "$prompt" == "help" ]; then
    echo "Available commands:"
    echo "  s  : Show the flashcard contents"
    echo "  q  : Quit the program"
    echo "  r ?: Remove a line by providing a line number"
    echo "  clr  : Clear terminal screen"
    echo "  sort : View the flashcards in sorted order Or Sort it"
    echo "  search ?: Search for a word in the flashcard"
    echo "  Once card is selected, Just Enter Topics to register"
  elif [ "$prompt" == "clr" ]; then
    clear
  elif [ "$prompt" == "s" ]; then
    bat "$dir"/"${selected_card}.txt"     # I know i said no dependency but please install bat or else you can use cat..but bat is objectively better
    #cat $dir/"${selected_card}.txt"    # use this line if u don't like or have bat
  elif [ "$prompt" == "r" ]; then
    if [ -n "$arg" ]; then
      sed -i "${arg}d" "$dir"/"${selected_card}.txt"
    else
      echo "Please provide a line to delete [r line_number]"
    fi
  elif [ "$prompt" == "sort" ]; then
    sort "$dir"/"${selected_card}.txt"
    echo ""
    echo "Are you sure about sorting the entries? (Y/n)"
    read prompt
    if [ "$prompt" == "Y" ]; then
      sort "$dir"/"${selected_card}.txt" -o "$dir"/"${selected_card}.txt"
    fi
  elif [ "$prompt" == "search" ]; then
    if [ -n "$arg" ]; then
      grep "$arg" "$dir"/"${selected_card}.txt"
    else
      echo "Please provide a search term! [search term]"
    fi
  else
    echo "$prompt" >> $dir/"${selected_card}.txt"
  fi
done

echo ""
echo ""
